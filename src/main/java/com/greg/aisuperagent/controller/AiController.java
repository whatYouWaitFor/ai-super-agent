package com.greg.aisuperagent.controller;

import com.greg.aisuperagent.agent.MyManus;
import com.greg.aisuperagent.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

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
     * 使用LoveApp
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/loveApp/chat")
    public Flux<String> doChatLoveApp(String message, String chatId) {
        return loveApp.doChatByStream(message, chatId);
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
