package com.greg.aisuperagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class myManusTest {

    @Resource
    private myManus myManus;
    @Test
    void run(){
        String userMessage = """
                我和老婆产生矛盾了，帮我找找网络上一些搞笑段子
                并结合图片，生成一个段子报告
                并以PDF格式输出
                """;

        String answer = myManus.run(userMessage);
        Assertions.assertNotNull(answer);
    }
}