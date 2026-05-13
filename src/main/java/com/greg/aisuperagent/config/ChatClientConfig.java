package com.greg.aisuperagent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 支持使用多个大模型供应商
 * 手动定义多个ChatClient(分别对应不同的chatModel大模型) Bean
 * 需spring.ai.chat.client.enabled=false取消ChatClient.Builder自动装配
 */
@Configuration
public class ChatClientConfig {
    @Bean
    public ChatClient dashScopeChatClient(ChatModel dashScopeChatModel) {
        return ChatClient.create(dashScopeChatModel);
    }

    @Primary
    @Bean
    public ChatClient openAiChatClient(ChatModel openAiChatModel) {
        return ChatClient.create(openAiChatModel);
    }
}
