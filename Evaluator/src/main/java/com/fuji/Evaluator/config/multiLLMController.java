package com.fuji.Evaluator.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.chat.client.autoconfigure.ChatClientBuilderConfigurer;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.logging.Logger;

@Configuration
public class multiLLMController {

    Logger logger = Logger.getLogger(multiLLMController.class.getName());

//    @Bean("openAIChatClient")
//    public ChatClient openAIChatClient(OpenAiChatModel openAiChatModel) {
//        return ChatClient.create(openAiChatModel);
//    }
//
//    @Bean("ollamaAIChatClient")
//    public ChatClient ollamaAIChatClient(OllamaChatModel ollamaChatModel) {
//        return ChatClient.create(ollamaChatModel);
//    }

    @Bean
    @Scope("prototype") // Important: allows different controllers to customize their own instance
    @Qualifier("openAiBuilder")
    public ChatClient.Builder openAiChatClientBuilder(
            ChatClientBuilderConfigurer configurer,
            OpenAiChatModel openAiChatModel) {
        logger.info("Building Using OpenAi Builder");
        ChatClient.Builder builder = ChatClient.builder(openAiChatModel).defaultAdvisors(new SimpleLoggerAdvisor());
        return configurer.configure(builder);
    }

    @Bean
    @Scope("prototype")
    @Qualifier("ollamaBuilder")
    public ChatClient.Builder ollamaChatClientBuilder(
            ChatClientBuilderConfigurer configurer,
            OllamaChatModel ollamaChatModel) {
        ChatOptions chatOptions = ChatOptions.builder().model("gemma3:1b").build();
        logger.info("Building Using the ollama Builder");
        ChatClient.Builder builder = ChatClient.builder(ollamaChatModel).defaultOptions(chatOptions).defaultAdvisors(new SimpleLoggerAdvisor());

        return configurer.configure(builder);
    }
}
