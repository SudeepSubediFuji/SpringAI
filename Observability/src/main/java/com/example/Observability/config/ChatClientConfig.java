package com.example.Observability.config;

import com.example.Observability.Rag.PIIMaskingDocumentPostProcessor;
import com.example.Observability.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder().maxMessages(10).chatMemoryRepository(jdbcChatMemoryRepository).build();
    }

    // Only defineing the chatClient as following as previously circular dependency
    // and other error are prune to grow
    @Bean("ChatMemory")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
            RetrievalAugmentationAdvisor retrievalAugmentationAdvisor, ChatMemory chatMemory) {
        ChatOptions chatOptions = ChatOptions.builder().model("gpt-5-mini").temperature(1.0).build();
        // ChatOptions chatOptions = ChatOptions.builder().build();
        Advisor chatmemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder.defaultOptions(chatOptions).defaultAdvisors(new SimpleLoggerAdvisor(),
                new TokenUsageAuditAdvisor(), retrievalAugmentationAdvisor, chatmemoryAdvisor).build();
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder) {
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).topK(3)
                        .similarityThreshold(0.5).build())
                .queryTransformers(TranslationQueryTransformer.builder().chatClientBuilder(chatClientBuilder.clone())
                        .targetLanguage("english").build())
                // .documentPostProcessors() -> we give a method as parameter to the
                // documentPostProcessors , which will define what should be used or not from
                // the documents(Eazybytes_HR_policy.pdf)
                // Such as hiding or masking the credential data such as the email or contact
                // (things that we wanna hide from normal users)
                // This helps to edit the info wanna provide to the client using Post retrieval
                // process of RAG.
                .documentPostProcessors(PIIMaskingDocumentPostProcessor.builder())
                .build();
    }
    // @Bean
    // ToolExecutionExceptionProcessor toolExecutionExceptionProcessor(){
    // return new DefaultToolExecutionExceptionProcessor(true);
    // }

}
