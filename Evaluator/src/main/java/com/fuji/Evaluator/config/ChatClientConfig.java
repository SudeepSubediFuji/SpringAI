package com.fuji.Evaluator.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder){
        return chatClientBuilder.build();
    }

//    @Bean
//    public FactCheckingEvaluator factCheckingEvaluator(ChatModel chatClientBuilder){
//        return new CustomFactCheckingEvalutor(chatClientBuilder);
//    }
}
