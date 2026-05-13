package com.greg.aisuperagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;


@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Value("classpath:LoveSystemPrompt.txt")
    private org.springframework.core.io.Resource systemPrompt;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮对话
        String message = "你好，我是佛山彭于晏";
        String answer = loveApp.doChat(message, chatId);
        // 第二轮
        /*message = "我想给我另一半(小冯)讲点冷笑话";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫啥来着？你记得吗";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第四轮
        message = "那我是谁";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);*/
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮对话
        String message = "你好，我是佛山彭于晏，我的另一半不开心，我想哄一下她，给我点建议";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
        System.out.println(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮对话
        String message = "我已经结婚了，但是最近产生了矛盾，给我点建议";
        String content = loveApp.doChatWithRag(message, chatId);
        System.out.println( content);
    }

    @Test
    void doChatWithRagCloud() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮对话
        String message = "我已经结婚了，但是最近产生了矛盾，给我点建议";
        String content = loveApp.doChatWithRag(message, chatId);
        System.out.println( content);
    }

    @Test
    void testDoChatWithRagCloud() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮对话
        String message = "我和老婆吵架了，应该怎么解决矛盾";
        String content = loveApp.doChatWithRagCloud(message, chatId);
    }


}