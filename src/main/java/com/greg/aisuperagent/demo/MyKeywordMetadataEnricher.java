package com.greg.aisuperagent.demo;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 基于 AI 的文档元信息增强器（这里会调ai解析出关键字）
 */
@Component
public class MyKeywordMetadataEnricher {

    @Resource
    private ChatModel openAiChatModel;
    
    public List<Document> enricherDocuments(List<Document> documents) {
        KeywordMetadataEnricher build = KeywordMetadataEnricher.builder(openAiChatModel).keywordCount(5).build();
        return build.apply(documents);
    }
}
