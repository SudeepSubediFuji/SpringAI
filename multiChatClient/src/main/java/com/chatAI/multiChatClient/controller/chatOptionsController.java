package com.chatAI.multiChatClient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chatOptions")
public class chatOptionsController {

    private final ChatClient simpleOpenAiChatClient;
    private final ChatClient openAISimpleChatClient;

    public chatOptionsController(@Qualifier("simpleOpenAiChatClient") ChatClient simpleOpenAiChatClient,
                                 @Qualifier("openAiChatClient") ChatClient openAISimpleChatClient) {
        this.simpleOpenAiChatClient = simpleOpenAiChatClient;
        this.openAISimpleChatClient = openAISimpleChatClient;
    }

    @GetMapping("/options")
    public String getChatOptions(@RequestParam ("message") String message) {
        return simpleOpenAiChatClient.prompt(message).advisors(new SimpleLoggerAdvisor()).call().content();
    }

    @GetMapping("/test")
    public Flux<ChatResponse> directOpenAi(@RequestParam("message") String message) {

        return openAISimpleChatClient.prompt(message)
                .options(OpenAiChatOptions.builder().model(OpenAiApi.ChatModel.CHATGPT_4_O_LATEST).temperature(0.7).maxTokens(5).build())
                .advisors(new SimpleLoggerAdvisor())
                .user("Hell:q" +
                        ":q!" +
                        ":qw" +
                        "o, can you provide a brief response?")
                .system("Act like Gork AI from X.")
                .stream().chatResponse();
    }



}
