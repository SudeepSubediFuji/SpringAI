package com.SpringAI.MultiChatModel.Config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class chatClientConfig {
    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel chatModel){
        return ChatClient.create(chatModel);
    }

//    @Bean
//    public ChatClient ollamaChatClient(Ollama)
}
