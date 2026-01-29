package com.example.SpringAIVector.config;

import io.qdrant.client.QdrantClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.openai.OpenAiChatModel;

@Configuration
public class AiConfig {

    @Bean("OpenAiChatClient")
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        ChatClient.Builder chatClient = ChatClient.builder(chatModel);
        return chatClient.build();
    }

//    @Bean
//    public VectorStore vectorStore(EmbeddingModel embeddingModel, QdrantClient qdrantClient) {
//        // This forces the creation of the Qdrant store
//        return new QdrantVectorStore(qdrantClient, "springAI", embeddingModel);
//    }

}
