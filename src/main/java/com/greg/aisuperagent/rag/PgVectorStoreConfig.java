package com.greg.aisuperagent.rag;


import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

/**
 * 配置 PgVector 向量数据库
 */
@Configuration
public class PgVectorStoreConfig {

    @Bean
    public VectorStore pgVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel openAiEmbeddingModel) {

        return PgVectorStore.builder(jdbcTemplate, openAiEmbeddingModel)
                .dimensions(1536)                    // 可选：默认为模型维度或 1536
                .distanceType(COSINE_DISTANCE)       // 可选：默认为 COSINE_DISTANCE
                .indexType(HNSW)                     // 可选：默认为 HNSW
                .initializeSchema(true)              // 可选：默认为 false
                .schemaName("public")                // 可选：默认为 "public"
                .vectorTableName("vector_store")     // 可选：默认为 "vector_store"
                .maxDocumentBatchSize(10000)         // 可选：默认为 10000
                .build();
    }
}
