package com.SpringAI.openAI.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatClientController {
    private final ChatClient chatClient;

    public ChatClientController(@Qualifier("openAiChatClientBuilder") ChatClient.Builder openAiChatClientBuilder){
        this.chatClient = openAiChatClientBuilder.build();
    }
    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message){
        // Prompt will handle the message as in the prompt given to the openai
        // Call will call the llm model
        // Here the content will give the response from LLM model
        return chatClient.prompt(message).call().content();
    }
}
