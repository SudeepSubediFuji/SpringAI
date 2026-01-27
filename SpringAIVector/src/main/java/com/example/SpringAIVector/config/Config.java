package com.example.SpringAIVector.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.openai.OpenAiChatModel;

@Configuration
public class Config {

    @Bean("OpenAiChatClient")
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        ChatClient.Builder chatClient = ChatClient.builder(chatModel);
        return chatClient.build();
    }

}
