package com.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class multiModelChatController {

    private final ChatClient OpenAiChatClient;
    private final ChatClient OllamaChatClient;

    public multiModelChatController(@Qualifier("OpenAiChatClient") ChatClient OpenAiChatClient,
                                          @Qualifier("OllamaChatClient") ChatClient OllamaChatClient){
        this.OpenAiChatClient = OpenAiChatClient;
        this.OllamaChatClient = OllamaChatClient;
    }

    @GetMapping("/openai")
    public String openAiChat(@RequestParam("") String message){
        return OpenAiChatClient.prompt(message).call().content();
    }

    @GetMapping("/ollama")
    public String ollamaAiChat(@RequestParam("") String message){
        return OllamaChatClient.prompt(message).call().content();
    }
}
