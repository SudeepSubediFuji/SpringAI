package com.example.SpringAIVector.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping
public class CompanyInfoAugumentedRetrivalAdvisorController {

    private final ChatClient chatClient;
    Logger logger = Logger.getLogger(CompanyInfoAugumentedRetrivalAdvisorController.class.getName());

    public CompanyInfoAugumentedRetrivalAdvisorController(ChatClient.Builder builder) {
        this.chatClient = builder.defaultAdvisors(List.of(new SimpleLoggerAdvisor())).build();
    }

    @GetMapping("/companyInfoRag")
    public ResponseEntity<String> companyBot(@RequestHeader("username") String username,
                                         @RequestParam("message") String message) {
        logger.info("CompanyInfoAugumentedRetrivalAdvisorController::RAG request received from user: " + username + " with message: " + message);
        String answer = chatClient.prompt(message)
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID,username))
                .user(message).call().content();
        logger.info("CompanyInfoAugumentedRetrivalAdvisorController::RAG answer generated for user: " + answer);
        return ResponseEntity.ok(answer);
    }
}
