package com.greg.aisuperagent.demo;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class SpringAIAlibabaDemo implements CommandLineRunner {

    @Resource
    private ChatModel dashScopeChatModel;

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;
    @Override
    public void run(String... args) throws Exception {
        /* ----------- Spring AI Alibaba创建ReactAgent方式调用大模型 ----------- */
        /*
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(apiKey)
                .build();
        // 初始化ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .defaultOptions(
                        DashScopeChatOptions.builder()
                        .model("qwen3.5-plus")
                        .build()
                )
                .dashScopeApi(dashScopeApi)
                .build();

        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                //.model(dashScopeChatModel)
                .instruction("You are a helpful weather forecast assistant.")
                .build();

        // 运行 Agent
        AssistantMessage call = agent.call("佛山的天气怎样?");
        System.out.println(call.getText());*/

        /* ----------- 使用OpenAI兼容的方式调用qwen ----------- */
        /*OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .openAiApi(
                    OpenAiApi.builder()
                            .apiKey(apiKey)
                            .build()
                ).build();
        ChatClient openAiChatClient = ChatClient.create(openAiChatModel);
        String content = openAiChatClient.prompt("你好，我是佛山彭于晏").call().content();
        System.out.println(content);

        var openAiApi = OpenAiApi.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();
        var openAiChatOptions = OpenAiChatOptions.builder()
                .model("gpt-3.5-turbo")
                .temperature(0.4)
                .maxTokens(200)
                .build();
        var chatModel = new OpenAiChatModel(this.openAiApi, this.openAiChatOptions);*/

    }


}

