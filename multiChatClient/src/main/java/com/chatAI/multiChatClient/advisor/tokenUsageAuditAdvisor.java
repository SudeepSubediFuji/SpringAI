package com.chatAI.multiChatClient.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

// An advisor to audit token usage from chat responses
// Here this class can only be implemented for OpenAI models that return token usage data
// Ollama and other models do not return token usage data in the response metadata as they run locally

public class tokenUsageAuditAdvisor implements CallAdvisor {

    private static Logger logger = LoggerFactory.getLogger(tokenUsageAuditAdvisor.class.getName());

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        ChatResponse chatResponse = chatClientResponse.chatResponse();
        if (chatResponse.getMetadata()!= null) {
            //Usage usage = (Usage) chatResponse.getMetadata().get("token_usage");
            Usage usage = chatResponse.getMetadata().getUsage();
            logger.info("Chat response metadata: {}", chatResponse.getMetadata().toString());
            if (usage!= null) {
                logger.info("Token Usage: Prompt Tokens: {}, Completion Tokens: {}, Total Tokens: {}, \nToken Usage Details: {}",
                        usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens(), usage.toString());
            } else {
                logger.warn("Token usage metadata is null.");
            }
        }else {
            logger.warn("Chat response metadata is null.");
        }
        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "tokenUsageAuditAdvisor";
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
