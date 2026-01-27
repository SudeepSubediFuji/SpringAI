package com.chatAI.multiChatClient.controller;

import com.chatAI.multiChatClient.service.TokenService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class ChatController {

    private final ChatClient chatClient;
    private final TokenService tokenService;
    Logger logger= Logger.getLogger(ChatController.class.getName());

    public ChatController(@Qualifier("OllamaChatClient") ChatClient chatClient, TokenService tokenService) {
        this.chatClient = chatClient;
        this.tokenService = tokenService;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String userInput) {
        // Calculate dynamic max tokens
        int dynamicMax = tokenService.calculateMaxTokens(userInput, 128000);
        ChatOptions chatOptions = ChatOptions.builder()
                .maxTokens(dynamicMax)
                .build();
        logger.info("Dynamic Max Tokens set to: " + dynamicMax);
        return chatClient.mutate()
                .defaultOptions(chatOptions)
                .build()
                .prompt(userInput)
                .call()
                .content();
    }
}
