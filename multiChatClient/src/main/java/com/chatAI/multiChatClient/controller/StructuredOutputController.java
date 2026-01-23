package com.chatAI.multiChatClient.controller;

import com.chatAI.multiChatClient.model.CountryCity;
import com.chatAI.multiChatClient.model.CountryRiver;
import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/structuredOutput")
public class StructuredOutputController {
    private final ChatClient ollamaOfficeChatClient;
    private final ChatClient simpleOpenAiChatClient;
    private final ChatClient openAiChatClient;

    public StructuredOutputController(@Qualifier("ollamaOfficeChatClient") ChatClient ollamaOfficeChatClient,
                                      @Qualifier("simpleOpenAiChatClient") ChatClient simpleOpenAiChatClient,
                                      @Qualifier("openAiChatClient") ChatClient openAiChatClient) {
        this.ollamaOfficeChatClient = ollamaOfficeChatClient;
        this.simpleOpenAiChatClient = simpleOpenAiChatClient;
        this.openAiChatClient = openAiChatClient;
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

    @GetMapping("/test3")
    public ResponseEntity<CountryRiver> getCountryRiver(@RequestParam("message") String message) {
        ChatOptions chatOptions = ChatOptions.builder().temperature(0.75).model("deepseek-r1:8b").build();
        CountryRiver response = ollamaOfficeChatClient.prompt(message).options(chatOptions).call().entity(CountryRiver.class);
        return ResponseEntity.ok(response);
    }

    // Entity Output with different Chat Options Example
    @GetMapping("/test4")
    public ResponseEntity<CountryRiver> getCountryRiver2(@RequestParam("message") String message) {
        ChatOptions chatOptions = ChatOptions.builder().temperature(0.4).model("gemma3:1b").build();
        CountryRiver response = ollamaOfficeChatClient.prompt(message).options(chatOptions).call().entity(CountryRiver.class);
        return ResponseEntity.ok(response);
    }

    // List Output Example
    @GetMapping("/test5")
    public ResponseEntity<List<String>> chatListCitys(@RequestParam("message") String message) {
        List<String> cities = simpleOpenAiChatClient.prompt(message).call().entity(new ListOutputConverter());
        return ResponseEntity.ok(cities);
    }

    // List Output Example
    @GetMapping("/test6")
    public ResponseEntity<Map<String,Object>> chatListCitys2(@RequestParam("message") String message) {
        Map<String,Object> cities = openAiChatClient.prompt(message).call().entity(new MapOutputConverter());
        return ResponseEntity.ok(cities);
    }


    // Bean Output Example
    @GetMapping("/test7")
    public ResponseEntity<CountryCity> chatListCitys3(@RequestParam("message") String message) {
        CountryCity cities = openAiChatClient.prompt(message).call().entity(new BeanOutputConverter<> (CountryCity.class));
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/test8")
    public ResponseEntity<List<CountryRiver>> chatListRivers(@RequestParam("message") String message) {
        List<CountryRiver> rivers = openAiChatClient.prompt(message).call().entity(new ParameterizedTypeReference<List<CountryRiver>>(){});
        return ResponseEntity.ok(rivers);
    }

}