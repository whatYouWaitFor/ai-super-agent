package com.greg.aisuperagent.agent;

import cn.hutool.core.util.StrUtil;
import com.greg.aisuperagent.agent.model.AgentState;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象基础代理类，用于管理代理状态和执行流程。
 *
 * 提供状态转换、内存管理和基于步骤的执行循环的基础功能。
 * 子类必须实现step方法。
 */
@Data
@Slf4j
public abstract class BaseAgent {

    // 智能体名称
    private String name;
    // 提示词
    private String systemPrompt;

    private String nextStepPrompt;
    // 状态
    private AgentState state = AgentState.IDLE;
    // 执行控制
    private int maxSteps = 10;
    private int currentStep = 0;
    // LLM
    private ChatClient chatClient;
    // Memory（自主维护会话上下文）
    private List<Message> messageList = new ArrayList<>();

    /**
     * 运行代理
     * @param userPrompt 用户提示词
     * @return 执行结果
     */
    public String run(String userPrompt) {
        if (state != AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent from state: " +  state);
        }
        if (StrUtil.isBlank(userPrompt)){
            throw new RuntimeException("Cannot run agent with empty user prompt");
        }
        // 更改状态
        state = AgentState.RUNNING;
        // 记录消息上下文
        messageList.add(new UserMessage(userPrompt));
        // 保存结果列表
        List<String> resultList = new ArrayList<>();
        try {
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                int stepNumber = i +1;
                currentStep = stepNumber;
                log.info("Executing step " + stepNumber  + "/" + maxSteps);
                String stepResult = step();
                String result = "Step " + stepNumber + ": " + stepResult;
                resultList.add(result);
            }
            // 检查是否超出步骤限制
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                resultList.add("Terminated: Reached max steps (" + maxSteps + ")");
            }
            return String.join("\n", resultList);
        } catch (Exception e) {
            state = AgentState.ERROR;
            log.error("Error executing agent: ", e);
            return "执行错误：" + e.getMessage();
        } finally {
            // 清理资源
            cleanUp();
        }
    }

    /**
     * 运行代理（流式返回结果）
     * @param userPrompt
     * @return
     */
    public SseEmitter runStream(String userPrompt) {
        // 创建SSEEmitter，设置5分钟超时时间
        SseEmitter sseEmitter = new SseEmitter(300000L);
        // 使用线程异步处理，避免阻塞主流程
        CompletableFuture.runAsync(() -> {
            try {
                if (state != AgentState.IDLE) {
                    sseEmitter.send("错误：无法从状态运行代理：" + this.state);
                    sseEmitter.complete();
                    return;
                }
                if (StrUtil.isBlank(userPrompt)){
                    sseEmitter.send("错误：不能使用空提示词运行代理");
                    sseEmitter.complete();
                    return;
                }
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
            // 更改状态
            state = AgentState.RUNNING;
            // 记录消息上下文
            messageList.add(new UserMessage(userPrompt));
            try {
                for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++) {
                    int stepNumber = i +1;
                    currentStep = stepNumber;
                    log.info("Executing step " + stepNumber  + "/" + maxSteps);
                    String stepResult = step();
                    String result = "Step " + stepNumber + ": " + stepResult;
                    // 发送没一步的结果
                    sseEmitter.send(result);
                }
                // 检查是否超出步骤限制
                if (currentStep >= maxSteps) {
                    state = AgentState.FINISHED;
                    sseEmitter.send("执行结束：达到最大步骤（" + maxSteps + "）");
                }
                // 正常完成
                sseEmitter.complete();
            } catch (Exception e) {
                state = AgentState.ERROR;
                try {
                    sseEmitter.send("执行错误：" + e.getMessage());
                    sseEmitter.complete();
                } catch (IOException ex) {
                    sseEmitter.completeWithError(ex);
                }
            } finally {
                // 清理资源
                cleanUp();
            }
        });

        // 设置超时回调
        sseEmitter.onTimeout(() -> {
            this.state = AgentState.ERROR;
            this.cleanUp();
            log.warn("SSE connection timed out");
        });
        // 设置完成回调
        sseEmitter.onCompletion(() -> {
            if (this.state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanUp();
            log.info("SSE connection completed");
        });
        return sseEmitter;
    }

    /**
     * 执行单个步骤
     * @return 步骤执行结果
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected void cleanUp() {

    }
}
