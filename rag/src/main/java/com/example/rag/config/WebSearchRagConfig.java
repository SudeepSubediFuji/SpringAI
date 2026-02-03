package com.example.rag.config;

import com.example.rag.Rag.WebSearchDocumentRetriever;
import com.example.rag.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
public class WebSearchRagConfig {

        @Bean("webSearchRagClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 RestClient.Builder restClientBuilder,
                                 ChatMemory chatMemory){
       QueryTransformer queryTransformer = TranslationQueryTransformer.builder()
               .chatClientBuilder(chatClientBuilder.clone())
               .targetLanguage("english")
               .build();

       var webSearchRagAdvisor =
               RetrievalAugmentationAdvisor.builder()
               .documentRetriever(WebSearchDocumentRetriever.builder().restClientBuilder(restClientBuilder).maxResults(5).build())
               .queryTransformers(queryTransformer)
               .build();
       Advisor chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(),webSearchRagAdvisor,new TokenUsageAuditAdvisor(),chatMemoryAdvisor))
                .build();
    }
}
