package com.chatAI.multiChatClient.controller;

import com.chatAI.multiChatClient.model.CountryCity;
import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/structuredOutput")
public class StructuredOutputController {
    private final ChatClient ollamaOfficeChatClient;
    private final ChatClient simpleOpenAiChatClient;

    public StructuredOutputController(@Qualifier("ollamaOfficeChatClient") ChatClient ollamaOfficeChatClient,
                                      @Qualifier("simpleOpenAiChatClient") ChatClient simpleOpenAiChatClient) {
        this.ollamaOfficeChatClient = ollamaOfficeChatClient;
        this.simpleOpenAiChatClient = simpleOpenAiChatClient;
    }
    @GetMapping("/test")
    public ResponseEntity<CountryCity> getCountryCity(@RequestParam("message") String message) {
        CountryCity countryCity = ollamaOfficeChatClient.prompt(message).advisors(new SimpleLoggerAdvisor()).call().entity(CountryCity.class);
        return ResponseEntity.ok(countryCity);
    }
    @GetMapping("/test2")
    public ResponseEntity<CountryCity> getCountryCity2(@RequestParam("message") String message) {
        CountryCity countryCity = simpleOpenAiChatClient.prompt(message).call().entity(CountryCity.class);
        return ResponseEntity.ok(countryCity);
    }
}
