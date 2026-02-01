package com.javatechie.advisor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AdutiTokenUsageAdvisor implements CallAdvisor {



    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        //call the next advisor //LLM
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        //Audit token usage here
        ChatResponse chatResponse = chatClientResponse.chatResponse();
        if(chatResponse !=null){
            Usage usage = chatResponse.getMetadata().getUsage();
            if(usage !=null){
              int inputTokens = usage.getPromptTokens();
              int completionTokens=  usage.getCompletionTokens();
                int totalTokens = usage.getTotalTokens();

                //log details
System.out.println("Total tokens:"+totalTokens+"     -Input tokens : "+inputTokens+"   output tokkens :"+completionTokens);

            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "AdutiTokenUsageAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
