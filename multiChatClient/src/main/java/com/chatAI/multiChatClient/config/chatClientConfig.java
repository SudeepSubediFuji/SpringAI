package com.chatAI.multiChatClient.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class chatClientConfig {

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){

        return ChatClient.create(openAiChatModel);
    }

//    @Bean
//    public ChatClient OllamaChatClient(OllamaChatModel ollamaChatModel){
//        return ChatClient.create(ollamaChatModel);
//    }

    //    // Rough part
    @Bean
    public ChatClient ollamaChatClientBuild(OllamaChatModel ollamaChatModel){
        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel).defaultSystem("""
                You are a coding helper AI, that only deals with code related inquires inside the company also following company's code of conduct.In the office hours, you only provide the information that are related to the office, no any joke or other topic related information.
                Also please start responding my saying "I am Fuji AI." and then in next line answer the question 
                """);


        return chatClientBuilder.build();
    }

    @Bean
    public ChatClient ollamaDevBotBuild(OllamaChatModel ollamaChatModel){
        ChatClient.Builder chatClientBuilder = ChatClient.builder(ollamaChatModel).defaultSystem("""
You are DevBot, a senior software engineer AI assistant.

EXPERTISE:
- Full-stack web development
- Cloud architecture and DevOps
- Database design and optimization
- API development and microservices

COMMUNICATION:
- Provide working code examples
- Explain decisions and trade-offs
- Include testing strategies
- Mention performance considerations

RESPONSE FORMAT:
- Start with brief explanation
- Show code with comments
- Include error handling
- Suggest next steps
""");
        return chatClientBuilder.build();
    }


}
