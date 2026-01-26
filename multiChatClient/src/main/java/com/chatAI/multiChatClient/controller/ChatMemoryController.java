package com.chatAI.multiChatClient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/chatMemory")
public class ChatMemoryController {
    private final ChatClient chatClient;
    private final ChatClient openAiChatClient;
    public ChatMemoryController(@Qualifier("chatMemoryChatClient") ChatClient chatClient,
                                @Qualifier("openAiChatMemoryChatClient") ChatClient openAiChatClient) {
        this.chatClient = chatClient;
        this.openAiChatClient = openAiChatClient;
    }
    @GetMapping("/1")
    public ResponseEntity<String> chatMemory(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {
        return ResponseEntity.ok(chatClient.prompt().user(message).advisors(
                advisorSpec -> advisorSpec.param(CONVERSATION_ID, username)
        ).call().content());
    }

    // Conversation using OpenAI Chat Client
    // Conversation_ID is set to username to maintain separate conversations per user
    // CONVERSATION_ID should be unique per user
    // InMemoryChatMemoryRepository will store the conversations in memory
    // So each user will have their own conversation history
    // This is useful for multi-user applications
    // Each user's conversation history will be maintained separately
    // Though this store is in-memory, so once the application restarts, all conversations will be lost
    // For accessing the conversatation ID with longlife , we will need to store these in the DB or file system
    @GetMapping("/2")
    public ResponseEntity<String> chatMemoryOpenAi(@RequestHeader("username") String username,
                                                   @RequestParam("message") String message) {
        return ResponseEntity.ok(openAiChatClient.prompt().user(message).advisors(
                advisorSpec -> advisorSpec.param(CONVERSATION_ID, username)
        ).call().content());
    }



}
