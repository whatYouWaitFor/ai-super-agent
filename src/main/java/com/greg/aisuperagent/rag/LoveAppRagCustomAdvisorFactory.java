package com.greg.aisuperagent.rag;


import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * 创建自定义的 RAG 检索增强顾问的工厂
 */
public class LoveAppRagCustomAdvisorFactory {

    public static Advisor createLoveAppRagCustomAdvisor(VectorStore pgVectorStore, String gender) {

        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(pgVectorStore)
                .filterExpression(new FilterExpressionBuilder().eq("gender", gender).build())//过滤条件
                .similarityThreshold(0.5)// 相似度阈值
                .topK(3) // 返回文档数量
                .build();

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .queryAugmenter(LoveAppContextQueryAugmentFactory.create())
                .build();
    }
}
