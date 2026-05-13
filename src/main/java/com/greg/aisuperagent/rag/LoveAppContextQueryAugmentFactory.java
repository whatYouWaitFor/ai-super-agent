package com.greg.aisuperagent.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * 创建一个上下文查询增强器工厂
 */
public class LoveAppContextQueryAugmentFactory {

    /**
     * 如果上下文为空，改写输出为模版内容
     * @return
     */
    public static ContextualQueryAugmenter create() {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("""
                你应该输出下面的内容：
                抱歉，我只能回答恋爱相关的问题，别的没办法帮到您哦，
                有问题可以联系佛山彭于晏
                """)
                .build();

        return ContextualQueryAugmenter.builder()
                .promptTemplate(promptTemplate)
                .allowEmptyContext(false)
                .build();
    }
}
