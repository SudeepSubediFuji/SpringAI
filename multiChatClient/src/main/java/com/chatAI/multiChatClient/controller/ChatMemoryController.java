package com.chatAI.multiChatClient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatMemory")
public class ChatMemoryController {
    private final ChatClient chatClient;
    public ChatMemoryController(@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    @GetMapping("/1")
    public ResponseEntity<String> chatMemory(@RequestParam("message") String message) {
        return ResponseEntity.ok(chatClient.prompt(message).call().content());
    }
}
