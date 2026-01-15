package com.SpringAI.openAI.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ollai")
public class DockerModelController {

    private final ChatClient chatClient;

    // The Builder is the best way to manage settings in Spring AI
    public DockerModelController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "Hi!") String message) {
        try {
            return chatClient.prompt(message).call().content();
        } catch (Exception e) {
            return "Error calling Ollama: " + e.getMessage();
        }
    }
}