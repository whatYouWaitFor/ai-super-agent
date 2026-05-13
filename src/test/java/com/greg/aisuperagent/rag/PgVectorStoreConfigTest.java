package com.greg.aisuperagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class PgVectorStoreConfigTest {

    @Resource
    private VectorStore pgVectorStore;

    @Test
    void test() {
        List<Document> documents = List.of(
                new Document("佛山彭于晏你好？请介绍佛山的美食", Map.of("meta1", "meta1")),
                new Document("佛山无影脚很厉害的"),
                new Document("怎么做美食.", Map.of("meta2", "meta2")));
        // 将文档添加到 PGVector
        pgVectorStore.add(documents);
        // 检索与查询相似的文档
        List<Document> results = this.pgVectorStore.similaritySearch(SearchRequest.builder().query("我来自佛山").topK(5).build());
        Assertions.assertNotNull(results);
    }
}