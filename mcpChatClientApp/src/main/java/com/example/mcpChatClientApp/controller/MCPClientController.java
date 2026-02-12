package com.example.mcpChatClientApp.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MCPClientController {

    private final ChatClient chatClient;

    public MCPClientController(ChatClient.Builder chatClientBuilder, ToolCallbackProvider mcpTool) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultToolCallbacks(mcpTool)
                .build();
    }

    @GetMapping("/chat")
    public String chat(
            @RequestParam("message") String message) {
        return chatClient.prompt().user(message).system("You are a github manager")
                .call().content();
    }

    @GetMapping("/createFolder")
    public String folderCreation(@RequestParam("message") String message) {
        return chatClient.prompt().user(message).call().content();
    }

}
