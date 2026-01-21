package com.chatAI.multiChatClient.controller;

import com.chatAI.multiChatClient.advisor.ollamaTokenUsageAuditAdvisor;
import com.chatAI.multiChatClient.advisor.tokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ollamaChatOptions")
public class ollamaChatOptionsController {

    // For testing Ollama 'Thinking' feature in chat options
    // Make sure to use a model that supports 'Thinking', e.g., "deepseek-r1:8b"
    // .advisors(List.of(new SimpleLoggerAdvisor(),new ollamaTokenUsageAuditAdvisor())) --> Add to debug advisor logs

    private ChatClient ollamaChatClientTest;
    public ollamaChatOptionsController(@Qualifier("ollamaChatClientTest") ChatClient ollamaChatClientTest) {
        this.ollamaChatClientTest = ollamaChatClientTest;
    }

    @GetMapping("/thinkMode" )
    public String ollamaChatOptionsTest(@RequestParam("message") String message) {
        OllamaChatOptions chatOptions = OllamaChatOptions.builder().model("deepseek-r1:8b").temperature(0.7).enableThinking().build();
        return ollamaChatClientTest.prompt(message).options(chatOptions).advisors(new ollamaTokenUsageAuditAdvisor()).call().content();
    }

    @GetMapping("/fastMode" )
    public String ollamaChatOptionsFastTest(@RequestParam("message") String message) {
        OllamaChatOptions chatOptions = OllamaChatOptions.builder().model("gemma3:1b").temperature(0.7).disableThinking().build();
        return ollamaChatClientTest.prompt(message).options(chatOptions).advisors(new ollamaTokenUsageAuditAdvisor()).call().content();
    }

}
