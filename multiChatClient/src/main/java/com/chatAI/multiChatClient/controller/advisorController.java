package com.chatAI.multiChatClient.controller;

import com.chatAI.multiChatClient.advisor.tokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import static com.sun.tools.jdeprscan.Main.call;

@RestController
@RequestMapping("/advisor")
public class advisorController {

    private final ChatClient ollamaOfficeChatClient;
    private final ChatClient openAISimpleChatClient;
//    private final ChatClient openAiChatClient;

    public advisorController(@Qualifier("ollamaOfficeChatClient") ChatClient ollamaOfficeChatClient,
                             @Qualifier("openAISimpleChatClient") ChatClient openAISimpleChatClient) {
        this.ollamaOfficeChatClient = ollamaOfficeChatClient;
        this.openAISimpleChatClient = openAISimpleChatClient;
    }

    @GetMapping("/office")
    public String ollamaofficeAdvisorChat(@RequestParam ("message") String message) {
        return ollamaOfficeChatClient.prompt(message).advisors(new SimpleLoggerAdvisor()).call().content();
    }

    @GetMapping("/fulloffice")
    public String ollamaofficeAdvisorChat2(@RequestParam ("message") String message) {
        return ollamaOfficeChatClient.prompt(message).advisors(new SimpleLoggerAdvisor()).call().content();
    }

//    @GetMapping("/fulloff")
//    public String ollamaofficeAdvisorChat3(@RequestParam ("message") String message) {
//        return ollamaOfficeChatClient.prompt(message)
//                .advisors(AdvisorSpec->
//                        AdvisorSpec.param("",new SimpleLoggerAdvisor()).call().content());
//    }

    @GetMapping("/openai")
    public String openAiAdvisorChat(@RequestParam ("message") String message) {
        return openAISimpleChatClient.prompt(message).call().content();
    }

    @GetMapping("/open")
    public String openAiAdvisorChat2(@RequestParam ("message") String message) {
        return ollamaOfficeChatClient.prompt(message).advisors(new tokenUsageAuditAdvisor()).call().content();
    }
}
