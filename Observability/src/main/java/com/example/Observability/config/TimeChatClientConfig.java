package com.example.Observability.config;

import com.example.Observability.Tools.DateTimeTools;
import com.example.Observability.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeChatClientConfig {

    // Only defineing the chatClient as following as previously circular dependency and other error are prune to grow
    @Bean("TimeChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,ChatMemory chatMemory){
        ChatOptions chatOptions = ChatOptions.builder().model("gpt-5-mini").temperature(1.0).build();
        Advisor chatmemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultOptions(chatOptions)
                .defaultTools(new DateTimeTools())
                .defaultAdvisors(new SimpleLoggerAdvisor(),new TokenUsageAuditAdvisor(),chatmemoryAdvisor)
                .build();
    }

}
