package com.fuji.Evaluator.controller;

import com.fuji.Evaluator.config.CustomFactCheckingEvalutor;
import com.fuji.Evaluator.exception.InvalidAnswerException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class WebController {
    // For our response generation
    public ChatClient openAIChatClient;
    // For evaluator to check the answer
    public ChatClient ollamaAIChatClient;
    public CustomFactCheckingEvalutor customFactCheckingEvalutor;
    public RelevancyEvaluator relevancyEvaluator;
    @Autowired
    public DataLoader dataLoader;

    @Value("classpath:promptTemplate/SystemPrompt.st")
    Resource systemPrompt;

    public WebController(@Qualifier("openAiBuilder") ChatClient.Builder chatClientBuilder,
                         @Qualifier("ollamaBuilder") ChatClient.Builder ollClientBuilder) {
        // opposite set
        this.openAIChatClient = ollClientBuilder.build();
        this.ollamaAIChatClient = chatClientBuilder.build();
        this.customFactCheckingEvalutor = new CustomFactCheckingEvalutor(chatClientBuilder);
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
        this.dataLoader = new DataLoader();
    }

    @GetMapping("/prompt")
    public String prompt(@RequestParam("message") String message) {
        return openAIChatClient.prompt().user(message).system(systemPrompt).call().content();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return openAIChatClient.prompt().user(message).call().content();
    }

    @Retryable(maxRetries = 3)
    @GetMapping("/evaluate")
    public String evaluate(@RequestParam("message") String message) {
        String aiResponse = openAIChatClient.prompt().user(message).call().content();
        validateAnswer(message, aiResponse);
        return "Data validation successfull! " + "\n" + aiResponse;
    }

    @GetMapping("/fcevaluate")
    public String evaluate2(@RequestParam("message") String message) {
        String aiResponse = openAIChatClient.prompt().user(message).call().content();
        validateAnswerWithFactChecking(message, aiResponse);
        return "Data validation successfull! " + "\n" + aiResponse;
    }

    private void validateAnswer(String question, String answer) {
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, List.of(), answer);
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        if (!evaluationResponse.isPass()) {
            throw new InvalidAnswerException(question, answer);
        }
    }

    private void validateAnswerWithFactChecking(String question, String answer) {
//        dataLoader.pdfLoader();
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, dataLoader.getJapanDoc(), answer);
        EvaluationResponse evaluationResponse = customFactCheckingEvalutor.evaluate(evaluationRequest);
        if (!evaluationResponse.isPass()) {
            throw new InvalidAnswerException(question, answer);
        }
    }



}
