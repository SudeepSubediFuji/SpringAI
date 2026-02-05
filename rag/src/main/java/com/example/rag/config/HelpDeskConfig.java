package com.example.rag.config;

import com.example.rag.Tools.HelpDeskTicketTools;
import com.example.rag.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class HelpDeskConfig {

    @Value("classpath:promptTemplate/HelpDeskAssistantPrompt.st")
    Resource HelpDeskPrompt;

    // Only defineing the chatClient as following as previously circular dependency and other error are prune to grow
    @Bean("HelpDesk")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, HelpDeskTicketTools helpDeskTicketTools) {
        ChatOptions chatOptions = ChatOptions.builder().model("gpt-5-mini").build();
        Advisor chatmemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return chatClientBuilder
                .defaultOptions(chatOptions)
                .defaultSystem(HelpDeskPrompt)
                .defaultTools(helpDeskTicketTools)
                .defaultAdvisors(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor(), chatmemoryAdvisor)
                .build();
    }

}
