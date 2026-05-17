package com.greg.aisuperagent.controller;

import com.greg.aisuperagent.agent.MyManus;
import com.greg.aisuperagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {
    
    @Resource
    private LoveApp loveApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel openAiChatModel;

    /**
     * 使用LoveApp（返回Flux对象）
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/loveApp/chatByFlux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatLoveApp(String message, String chatId) {
        return loveApp.doChatByStream(message, chatId);
    }

    @GetMapping("/loveApp/chat")
    public SseEmitter doChatLoveAppSseEmitter(String message, String chatId) {
        SseEmitter emitter = new SseEmitter(180000L);
        // 获取Flux数据流并直接订阅
        loveApp.doChatByStream(message, chatId)
                .subscribe(
                        // 处理每条消息
                        chunk -> {
                            try {
                                emitter.send(chunk);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                            }
                        },
                        // 处理错误
                        emitter::completeWithError,
                        // 处理完成
                        emitter::complete);
        return emitter;
    }

    /**
     * 使用AI超级智能体
     * @param message
     * @return
     */
    @GetMapping("/myManus/chat")
    public SseEmitter doChatWithManus(String message) {
        MyManus manus = new MyManus(allTools, openAiChatModel);
        return manus.runStream(message);
    }
}
