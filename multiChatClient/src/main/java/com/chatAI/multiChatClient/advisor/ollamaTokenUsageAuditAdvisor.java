package com.chatAI.multiChatClient.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

public class ollamaTokenUsageAuditAdvisor implements CallAdvisor {

    Logger logger = LoggerFactory.getLogger(ollamaTokenUsageAuditAdvisor.class);
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        ChatResponse chatResponse = chatClientResponse.chatResponse();
        logger.info("Ollama Chat Client Response Metadata: {}", chatClientResponse.chatResponse().getMetadata());
        if (chatResponse.getMetadata()!= null) {
            Usage usage = chatResponse.getMetadata().getUsage();
            if (usage!= null) {
                logger.info("Ollama Token Usage: Prompt Tokens: {}, Completion Tokens: {}, Total Tokens: {}, Native Tokens: {} \nToken Usage Details: {}",
                        usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens(), usage.getNativeUsage(), usage.toString());
                String thinking = chatResponse.getResult().getMetadata().get("thinking");
                if (thinking != null) {
                    logger.info("Ollama 'Thinking' Metadata: {}", thinking);
                }
            } else {
                logger.warn("Ollama Token usage metadata is null.");
            }
            logger.info("Ollama Chat Response Metadata: {}", chatResponse.getMetadata().toString());
        } else {
            logger.warn("Ollama Chat response metadata is null.");
        }

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "ollamaTokenUsageAuditAdvisor";
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
