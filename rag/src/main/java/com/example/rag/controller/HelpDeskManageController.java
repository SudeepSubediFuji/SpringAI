package com.example.rag.controller;

import com.example.rag.Tools.HelpDeskTicketTools;
import com.example.rag.Tools.TicketCreate;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping
public class HelpDeskManageController {
    private final ChatClient chatClient;
    private final HelpDeskTicketTools helpDeskTicketTools;
    private final TicketCreate ticketCreate;

    public HelpDeskManageController(@Qualifier("HelpDesk") ChatClient chatClient, HelpDeskTicketTools helpDeskTicketTools, TicketCreate ticketCreate) {
        this.chatClient = chatClient;
        this.helpDeskTicketTools = helpDeskTicketTools;
        this.ticketCreate=ticketCreate;
    }

    @GetMapping("/help")
    public ResponseEntity<String> HelpDeskBot(@RequestHeader("username") String username,
                                              @RequestParam("message") String message) {
        String answer = chatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                .tools(helpDeskTicketTools)
                .toolContext(Map.of("username", username))
                .call().content();
        return ResponseEntity.ok(answer);
    }
    //TicketCreate
    @GetMapping("/helps")
    public ResponseEntity<String> HelpDeskBot2(@RequestHeader("username") String username,
                                              @RequestParam("message") String message) {
        String answer = chatClient.prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, username))
                .tools(ticketCreate)
                .toolContext(Map.of("username", username))
                .call().content();
        return ResponseEntity.ok(answer);
    }

}
