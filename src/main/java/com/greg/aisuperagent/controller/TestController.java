package com.greg.aisuperagent.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    private final ChatClient openAiChatClient;

//    public TestController(ChatClient.Builder chatClientBuilder) {
//        this.openAiChatClient = chatClientBuilder.build();
//    }

    // 也可以使用如下的方式注入 ChatClient
    public TestController(ChatModel openAiChatModel) {
        this.openAiChatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem(DEFAULT_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/simpleChat")
    public Map<String, Object> simpleChat(String message) {
        String content = openAiChatClient.prompt(message).call().content();

        return Map.of(
                "response", content,
                "timestamp", Instant.now().toString());
    }
}
