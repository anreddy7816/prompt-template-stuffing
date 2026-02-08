package com.javatechie.service;

import com.javatechie.advisor.AdutiTokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderSupportAIAssistantService {

    @Value("classpath:prompts/order_system_template.st")
    private Resource orderSystemPrompt;

    @Value("classpath:prompts/order_user_template.st")
    private Resource orderUserPrompt;

    @Value("classpath:prompts/order_system_policy.st")
    private Resource orderSystemPolicyPrompt;


    private final ChatClient chatClient;

    public OrderSupportAIAssistantService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    public String assistWithOrderSupport(String customerName, String orderId, String customerMessage) {
        return chatClient
                .prompt()
                .system(orderSystemPrompt)
                .user(promptUserSpec -> promptUserSpec.text(orderUserPrompt)
                        .param("customerName", customerName)
                        .param("orderId", orderId)
                        .param("customerMessage", customerMessage))
                .call()
                .content();
    }

    public String talkToAISupport(String customerName, String orderId, String customerMessage) {

        ChatOptions build = ChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.3) // 0.3 for coding -> more value defines mode randomness
                .maxTokens(200) // max number tokens in the response
                .frequencyPenalty(0.7) // If response has more repeted words , restrict them.
                .presencePenalty(0.7) //  don't repeat if word is present.
                .stopSequences(List.of("3")) // If you want stop response when there is particular word found.
                .topK(3) // top 3
                .topP(0.1)
                .build();

        return chatClient
                .prompt()
                .options(build)
                .advisors(List.of(new SimpleLoggerAdvisor(),new SafeGuardAdvisor(List.of("password","otp","cvv","bomb"),"For Security reason we never ask such sensitive information, please talk to our support executive",1),new AdutiTokenUsageAdvisor()))
                .system(orderSystemPolicyPrompt)
                .user(promptUserSpec -> promptUserSpec.text(orderUserPrompt)
                        .param("customerName", customerName)
                        .param("orderId", orderId)
                        .param("customerMessage", customerMessage))
                .call()
                .content();
    }

}
