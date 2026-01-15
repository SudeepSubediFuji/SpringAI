package com.chatAI.multiChatClient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class chatClientController {

    private final ChatClient openAiChatClient;
    private final ChatClient ollamaChatClientBuild;
    public chatClientController(@Qualifier("openAiChatClient") ChatClient openAiChatClient,
                                @Qualifier("ollamaChatClientBuild") ChatClient ollamaChatClientBuild){
        this.openAiChatClient = openAiChatClient;
        this.ollamaChatClientBuild = ollamaChatClientBuild;
    }

    @GetMapping("/openai")
    public String openAiChat(@RequestParam("message") String message){
        return openAiChatClient.prompt(message).call().content();
    }

    @GetMapping("/ollama")
    public String ollamaAiChat(@RequestParam("message") String message){
        return ollamaChatClientBuild.prompt(message).call().content();
    }

}
