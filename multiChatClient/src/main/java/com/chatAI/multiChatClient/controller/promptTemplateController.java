package com.chatAI.multiChatClient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prompt")
public class promptTemplateController {

    private final ChatClient ollamaChatClientBuild;
    private final ChatClient openAiChatClient;

    public promptTemplateController(ChatClient ollamaChatClientBuild, ChatClient openAiChatClient) {
    this.ollamaChatClientBuild = ollamaChatClientBuild;
    this.openAiChatClient = openAiChatClient;
    }

    String emailTemplate = """
            You are an expert customer service representative. 
            Use the following template to respond to customer inquiries in a polite and professional manner.

            Template:
            Dear {customerName},

            Thank you for reaching out to us. We appreciate your interest in our services.

            {responseBody}

            If you have any further questions or need additional assistance, please do not hesitate to contact us.

            Best regards,
            [Your Company Name] Customer Service Team
            """;

    String promptTemplate = """
            The customer named {customerName} has sent the following message:
            "{customerMessage}"

            Please draft a response using the email template provided, ensuring to address the customer's concerns and provide helpful information.
            """;

    @Value("classpath:promptTemplates/userPromptTemplate.st")
    Resource userPromptTemplate;

    @GetMapping("/email" )
    public String emailResponse(@RequestParam("customerName") String customerName,
                                @RequestParam("customerMessage") String customerMessage){
        return ollamaChatClientBuild.prompt()
                .system(emailTemplate)
                .user(PromptTemplateSpec->
                        PromptTemplateSpec.text(userPromptTemplate)
                                .param("customerName",customerName)
                                .param("customerMessage",customerMessage))
                .call().content();
    }

    @GetMapping("/emails" )
    public String openEmailResponse(@RequestParam("customerName") String customerName,
                                @RequestParam("customerMessage") String customerMessage){
        return openAiChatClient.prompt()
                .system(emailTemplate)
                .user(PromptTemplateSpec->
                        PromptTemplateSpec.text(userPromptTemplate)
                                .param("customerName",customerName)
                                .param("customerMessage",customerMessage))
                .call().content();
    }

}
