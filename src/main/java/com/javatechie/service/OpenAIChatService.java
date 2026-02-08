package com.javatechie.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class OpenAIChatService {

    private final ChatClient chatClient;

    public OpenAIChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }


    public Flux<String> chatWithOpenAILLM(String message) {
//        ChatOptions build = ChatOptions.builder()
//                .model("gpt-4o-mini")
//                .temperature(0.3) // 0.3 for coding -> more value defines mode randomness
//                .maxTokens(100) // max number tokens in the response
//                .frequencyPenalty(0.7) // If response has more repeted words , restrict them.
//                .presencePenalty(0.7) //  don't repeat if word is present.
//                .stopSequences(List.of("3")) // If you want stop response when there is particular word found.
////                .topK(3) // top 3
//                .topP(0.1)
//                .build();
//        return chatClient
//                //all type message roles (Prompt)
//                .prompt(message)
////                .options(build)
//                .call()
//                .content();

        return chatClient
                //all type message roles (Prompt)
                .prompt(message)
//                .options(build)
                .stream().content();

    }
}
