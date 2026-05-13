package com.greg.aisuperagent.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * 查询重写器
 */
@Component
public class MyRewriteQueryTransformer {

    private final QueryTransformer queryTransformer;

    public MyRewriteQueryTransformer (ChatModel openAiChatModel) {
        ChatClient.Builder builder = ChatClient.builder(openAiChatModel);
        queryTransformer  = RewriteQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    /**
     * 转换查询文本
     * @param message
     * @return
     */
    public String transform(String message) {
        Query query = new Query(message);
        // 执行查询重写
        Query transform = queryTransformer.transform(query);
        // 输出重写后的查询
        return transform.text();
    }
}
