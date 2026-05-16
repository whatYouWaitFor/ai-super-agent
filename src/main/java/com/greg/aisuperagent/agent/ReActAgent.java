package com.greg.aisuperagent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * ReAct (Reasoning and Acting) 模式的代理抽象类
 * 实现了思考-行动的循环模式
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public abstract class ReActAgent extends BaseAgent {

    /**
     * 处理当前状态并决定下一步行动
     * @return 是否需要执行行动，true表示需要执行行动，false表示不需要执行行动
     */
    public abstract boolean think();

    /**
     * 执行j决定的行动
     * @return 行动结果
     */
    public abstract String act();

    @Override
    public String step() {
        try {
            boolean shouldAct = think();
            if (!shouldAct) {
                return "思考完成 - 无需行动";
            }
            return act();
        } catch (Exception e) {
            log.error("步骤执行失败：", e);
            return "步骤执行失败：" + e.getMessage();

        }
    }
}
