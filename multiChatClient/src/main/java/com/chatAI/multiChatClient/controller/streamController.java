package com.chatAI.multiChatClient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stream")
public class streamController {

    private final ChatClient OllamaChatClient;
    public streamController(ChatClient OllamaChatClient)   {
        this.OllamaChatClient= OllamaChatClient;
    }

    @GetMapping("/string")
    public Flux<String> streamString(@RequestParam("message") String message) {
        return OllamaChatClient.prompt(message).stream().content();
    }

    @GetMapping("/chatResponse")
    public Flux<ChatResponse> streamChatResponse(@RequestParam("message") String message) {
        return OllamaChatClient.prompt(message).stream().chatResponse();
    }

    @GetMapping("/chatClientResponse")
    public Flux<ChatClientResponse> streamChatClientResponse(@RequestParam("message") String message) {
        return OllamaChatClient.prompt(message).stream().chatClientResponse();
    }


}
