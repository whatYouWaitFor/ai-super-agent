package com.greg.aisuperagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
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


    @Test
    void doChatWithTool() {
        // 测试联网搜索问题的答案
        //testMessage("周末想带老婆去佛山约会，推荐几个适合情侣的小众打卡地，结合网上最新的评价？");

        // 测试网页抓取：恋爱案例分析
        //testMessage("最近和对象吵架了，抓取编程导航网站的网页内容,url是codefather.cn，看看其他情侣是怎么解决矛盾的？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");

        // 测试终端操作：执行代码
        //testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        //testMessage("生成我的恋爱档案文件");

        // 测试 PDF 生成
        //testMessage("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTool(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "我要提取一下https://www.baidu.com/网页内容，use firecrawl" ;
        String answer = loveApp.doChatWithMcp(message, chatId);
        System.out.println( answer);
    }

    @Test
    void doChatWithSearchPhotoMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我搜索一下好看的风光照片 use image-search-mcp-server" ;
        String answer = loveApp.doChatWithMcp(message, chatId);System.out.println( answer);
        System.out.println( answer);
    }
}