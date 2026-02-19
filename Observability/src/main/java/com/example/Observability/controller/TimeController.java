package com.example.Observability.controller;

import com.example.Observability.Tools.DateTimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping
public class TimeController {
    private final ChatClient chatClient;

    public TimeController(@Qualifier("TimeChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/time")
    public ResponseEntity<String> getTime(@RequestHeader("username") String username,
                                          @RequestParam("message") String message) {
        String answer = chatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                .tools(new DateTimeTool())
                .call().content();
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/time2")
    public ResponseEntity<String> getLocaleTime(@RequestHeader("username") String username,
                                                @RequestParam("message") String message) {
        String answer = chatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
//                .tools(new DateTimeTools())
                .call().content();
        return ResponseEntity.ok(answer);
    }
}
