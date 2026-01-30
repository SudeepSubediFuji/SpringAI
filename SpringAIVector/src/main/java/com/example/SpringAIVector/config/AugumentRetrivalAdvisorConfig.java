package com.example.SpringAIVector.config;//package com.example.SpringAIVector.config;
//
//import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
//import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
//
//@Configuration
//public class AugumentRetrivalAdvisorConfig {
//
//    @Bean
//    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore){
//        return RetrievalAugmentationAdvisor.builder()
//                .documentRetriever(VectorStoreDocumentRetriever.builder()
//                        .vectorStore(vectorStore)
//                        .similarityThreshold(0.5)
//                        .topK(3)
//                        .build()).build();
//    }
//}
