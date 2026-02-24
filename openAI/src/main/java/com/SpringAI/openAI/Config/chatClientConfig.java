package com.SpringAI.openAI.Config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class chatClientConfig {
    @Bean("openAiChatClientBuilder")
    public ChatClient.Builder openAiChatClientBuilder(OpenAiChatModel chatModel){
        return ChatClient.builder(chatModel);
    }
    @Bean("ollamaChatClientBuilder")
    public ChatClient.Builder ollamaChatClientBuilder(OllamaChatModel chatModel){
        return ChatClient.builder(chatModel);
    }

}
