package com.example.SpringAIVector.config;

import io.qdrant.client.QdrantClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.openai.OpenAiChatModel;

@Configuration
public class AiConfig {

    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore){
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .similarityThreshold(0.5)
                        .topK(3)
                        .build()).build();
    }

    @Bean("OpenAiChatClient")
    public ChatClient chatClient(OpenAiChatModel chatModel , RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
        ChatClient.Builder chatClient = ChatClient.builder(chatModel).defaultAdvisors(retrievalAugmentationAdvisor);
        return chatClient.build();
    }

    @Bean("aiConfiguration")
    public ChatClient chatClient2(ChatClient.Builder chatClientBuilder,RetrievalAugmentationAdvisor  retrievalAugmentationAdvisor) {
        return chatClientBuilder
                .defaultAdvisors(retrievalAugmentationAdvisor).build();
    }

//    @Bean
//    public VectorStore vectorStore(EmbeddingModel embeddingModel, QdrantClient qdrantClient) {
//        // This forces the creation of the Qdrant store
//        return new QdrantVectorStore(qdrantClient, "springAI", embeddingModel);
//    }

}
