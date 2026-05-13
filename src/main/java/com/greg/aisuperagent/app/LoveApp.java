package com.greg.aisuperagent.app;

import com.greg.aisuperagent.advisor.MyLoggerAdvisor;
import com.greg.aisuperagent.demo.MyRewriteQueryTransformer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient openAiChatClient;

    @Resource
    private ChatClient dashScopeChatClient;

//    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
//            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
//            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
//            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    private static final String SYSTEM_PROMPT = """
            你是 **恋爱大师**，一位深耕依恋理论、非暴力沟通的资深关系顾问。风格：**三分共情、两分洞察、一点幽默**。你从不评判，用“我们”的同盟语气，通过**场景模拟**和**引导式提问**，逐步走进用户内心，共创可落地的行动方案。
            
            **工作流（自然融入对话，不机械）**
            1. **接住情绪**：先共情，让对方感觉被理解。
            2. **场景具象化**：把模糊困扰变成具体画面。主动构建恋爱、相处、婚后场景，比如：“很多情侣在冷静期会反复试探对方心意，你们有过那种‘明明想靠近，开口却带刺’的时刻吗？”
            3. **探问与洞察**：每次回复**至少嵌入1-2个问题**，围绕：
               - **情绪纹理**：“那种感觉像什么颜色？底下藏着受伤还是恐惧？”
               - **真实需求**：“你最渴望他做的一个小动作是什么？想要解决还是被倾听？”
               - **隐藏模式**：“你们常跳‘追-逃’的舞步吗？你父母的冲突方式有没有影子在你身上？”
               - **微小行动**：“为了让局面松动1%，明天可以试着用‘我需要…’开头说一句话吗？”
            4. **共创策略**：给出话术脚本或模拟演练，绝不空谈。
            
            **记忆与边界**：记住伴侣称呼、关系阶段、核心痛点和依恋倾向，自然提及建立连续感。绝不评判、不说“你应该”。察觉自伤危机时，立即切换共情模式，提供心理援助热线（400-161-9995），停止策略建议。
            
            **开场白**：“嗨，我是你的恋爱大师。不管揣着甜蜜烦恼还是心酸，这儿都是安全树洞。随便从一个场景、心情，或堵在胸口的话开始吧……今天想和我聊什么？”
            """;

    /**
     * 构造器初始化ChatClient
     * @param openAiChatModel
     */
    public LoveApp(ChatModel openAiChatModel) {
        // 初始化消息聊天窗口，默认用内存仓库
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                // 保留最近10条消息
                .maxMessages(10)
                .build();
        this.openAiChatClient = ChatClient.builder(openAiChatModel)
                //.defaultSystem(new InputStreamResource(getClass().getClassLoader().getResourceAsStream("system.txt")))
                //.defaultSystem(systemPrompt)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        // 自定义日志Advisor
                        new MyLoggerAdvisor()
                        // 自定义推理增强Advisor，按需开启
                        //,new ReReadingAdvisor()
                )
                .build();
    }

    /**
     * AI基础对话（支持多轮对话记忆）
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = openAiChatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }


    record LoveReport(String title, List<String> suggestion){};

    /**
     * 结构化输出生成报告
     * @param message
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String message, String chatId) {
        LoveReport loveReport = openAiChatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                //.system(SYSTEM_PROMPT)
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(LoveReport.class);
        return loveReport;
    }

    @Resource
    private VectorStore vectorStore;

    @Resource
    private MyRewriteQueryTransformer myRewriteQueryTransformer;

    /**
     * 使用本地知识库
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(String message, String chatId) {
        String rewriteMessage = myRewriteQueryTransformer.transform(message);

        ChatResponse chatResponse = openAiChatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())

                // 使用自定义检索增强顾问
                //.advisors(LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(vectorStore, "male"))


                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }

    //@Resource
    private Advisor loveAppRagCloudAdvisor;

    /**
     * 使用阿里云知识库
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRagCloud(String message, String chatId) {
        ChatResponse chatResponse = openAiChatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(loveAppRagCloudAdvisor)
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }

    public String doChatWithTool(String message, String chatId) {
        ChatResponse chatResponse = openAiChatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(loveAppRagCloudAdvisor)
                .call()
                .chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        return text;
    }

}
