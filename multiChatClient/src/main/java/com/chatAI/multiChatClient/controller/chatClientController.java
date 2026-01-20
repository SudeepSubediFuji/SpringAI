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
    private final ChatClient ollamaDevBotBuild;
//    private final ChatClient
    public chatClientController(@Qualifier("openAiChatClient") ChatClient openAiChatClient,
                                @Qualifier("ollamaChatClientBuild") ChatClient ollamaChatClientBuild,
                                @Qualifier("ollamaDevBotBuild") ChatClient ollamaDevBotBuild) {
        this.openAiChatClient = openAiChatClient;
        this.ollamaChatClientBuild = ollamaChatClientBuild;
        this.ollamaDevBotBuild = ollamaDevBotBuild;

    }

    @GetMapping("/openai")
    public String openAiChat(@RequestParam("message") String message){
        return openAiChatClient.prompt(message).call().content();
    }

    @GetMapping("/ollama")
    public String ollamaAiChat(@RequestParam("message") String message){
        return ollamaChatClientBuild.prompt(message).call().content();
    }

    // 会社中だけの相談でけるようなカスタムAI
    //.system --> System Role --> message set
    @GetMapping("/ollamaoffice")
    public String ollamaofficeAiChat(@RequestParam("message") String message){
        return ollamaChatClientBuild.prompt().user(message).call().content();
    }

    @GetMapping("/openoffice")
    public String openAIofficeAiChat(@RequestParam("message") String message){
        return openAiChatClient.prompt().user(message).system("""
    You are a internal management AI.That will only deal with office related queries. No any funny business. 
    """).call().content();
    }

    @GetMapping("/devbot")
    public String devBotAiChat(@RequestParam("message") String message){
        return ollamaDevBotBuild.prompt().user(message).call().content();
        }
}
