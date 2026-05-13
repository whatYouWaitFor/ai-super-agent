package com.greg.aisuperagent.rag;

import com.greg.aisuperagent.demo.MyKeywordMetadataEnricher;
import com.greg.aisuperagent.demo.MyTokenTextSplitter;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 恋爱大师向量数据库配置（基于内存的向量数据库）
 * ps：这里先注释，免得每次启动项目就创建bean，浪费token
 */
//@Configuration
public class LoveAppSimpleVectorStoreConfig {

    @Resource
    private LoveAppMarkdownReader markdownReader;
    
    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyKeywordMetadataEnricher myKeywordMetadataEnricher;

    @Bean
    public VectorStore vectorStore(EmbeddingModel openAiEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(openAiEmbeddingModel).build();
        // 加载文档
        List<Document> documents = markdownReader.loadMarkdown();
        // 自定义切分文档
        //List<Document> documents1 = myTokenTextSplitter.splitWithBuilder(documents);
        // 自定义解析关键字并添加元数据
        //List<Document> documents2 = myKeywordMetadataEnricher.enricherDocuments(documents);

        // 在写入数据库前，先调用embedding大模型将文档转为向量
        //simpleVectorStore.doAdd(documents);

        return simpleVectorStore;
    }
}
