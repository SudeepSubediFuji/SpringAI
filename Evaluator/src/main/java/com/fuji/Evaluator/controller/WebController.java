package com.fuji.Evaluator.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class WebController {
    public ChatClient chatClient;

    @Value("classpath:promptTemplate/SystemPrompt.st")
    Resource systemPrompt;

    public WebController(ChatClient.Builder chatClientBuilder){
        this.chatClient=chatClientBuilder.build();
    }

    @GetMapping("/prompt")
    public String prompt(@RequestParam("message") String message){
        return chatClient.prompt().user(message).system(systemPrompt).call().content();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message){
        return chatClient.prompt().user(message).call().content();
    }

}
