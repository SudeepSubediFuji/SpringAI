package com.example.SpringAIVector.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class chatClientController {

    private final ChatClient chatClient;

    public chatClientController(@Qualifier("OpenAiChatClient") ChatClient chatClient){
        this.chatClient=chatClient;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String userInput) {
        return chatClient.prompt(userInput).call().content();
    }
}
