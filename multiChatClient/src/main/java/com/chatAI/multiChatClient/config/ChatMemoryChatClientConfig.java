package com.chatAI.multiChatClient.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatMemoryChatClientConfig {

    @Bean("chatMemoryChatClient")
    public ChatClient ChatMemoryChatClient(@Qualifier("ollamaChatMemoryClientBase") ChatClient baseClient, ChatMemory chatMemory) {
        Advisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return baseClient.mutate().defaultAdvisors(chatMemoryAdvisor,new SimpleLoggerAdvisor()).build();
    }

    @Bean("openAiChatMemoryChatClient")
    public ChatClient ChatMemoryOpenAiChatClient(@Qualifier("openAiChatMemoryClientBase") ChatClient baseClient, ChatMemory chatMemory) {
        Advisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return baseClient.mutate().defaultAdvisors(chatMemoryAdvisor,new SimpleLoggerAdvisor()).build();
    }
}
