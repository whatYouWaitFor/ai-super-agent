package com.greg.aisuperagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取LoveApp的Markdown文件
 */
@Component
@Slf4j
public class LoveAppMarkdownReader {

    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppMarkdownReader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public List<Document> loadMarkdown() {
        List<Document> documentList = new ArrayList<>();
        try {
            // 加载多个Markdown文件
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", filename)
                        .build();

                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                documentList.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("加载markdown文件失败: {}", e);
        }
        return documentList;
    }
}
