package com.fuji.Evaluator;

import com.fuji.Evaluator.config.CustomFactCheckingEvalutor;
import com.fuji.Evaluator.controller.DataLoader;
import com.fuji.Evaluator.controller.WebController;
import org.junit.jupiter.api.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
        "spring.ai.openai.api-key=${OPENAI_API_KEY:test-key}",
        "logging.level.org.springframework.ai.chat.client.advisor=DEBUG"
})
@DisplayName("Evaluator Application Tests")
class EvaluatorApplicationTests {
    private final float Perfect_Score = 1.0f;
    private final List<String> groundTruthList = List.of("Tokyo is the capital of Japan.");
    private final Document groundTruthDocument = new Document(groundTruthList.get(0));

    Logger logger = Logger.getLogger(EvaluatorApplicationTests.class.getName());

    @Autowired
    private DataLoader dataLoader;

    @Autowired
    private WebController webController;
    @Autowired
    private OpenAiChatModel chatModel;
    private ChatClient chatClient;
    private RelevancyEvaluator relevancyEvaluator;
    private CustomFactCheckingEvalutor factCheckingEvaluator;
    private FactCheckingEvaluator.Builder factCheckingEvaluatorBuilder;
    @Value("${evaluator.minRelivenceScore:0.7}")
    private float minRelivenceScore;

    @Value("classpath:/promptTemplate/SystemPrompt.st")
    Resource HrPolicy;

    @BeforeEach
    void setUp() {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel).defaultAdvisors(new SimpleLoggerAdvisor());
        this.chatClient = chatClientBuilder.build();
        this.relevancyEvaluator = new RelevancyEvaluator(chatClientBuilder);
        this.factCheckingEvaluator = new CustomFactCheckingEvalutor(chatClientBuilder);


    }

    @Test
    @DisplayName("relevancyEvaulator Test1")
    @Timeout(value = 30)
    void WebControllerRelevenceEvaluator() {
        // Given
        String question = "What is the capital of Japan?";
        // Ai response from the prompt method of WebController
        String aiResponse = webController.chat(question);

        EvaluationRequest evaluationRequest = new EvaluationRequest(question, aiResponse);
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        Assertions.assertAll(() -> assertThat(aiResponse).isNotBlank(),
                () -> assertThat(evaluationResponse.isPass())
                        .withFailMessage(
                                "Expected the AI response to be relevant to the question, but it was not. " +
                                        "Question: %s, AI Response: %s".formatted(question, aiResponse))
                        .isTrue()
        );
        Assertions.assertEquals(Perfect_Score, evaluationResponse.getScore());
    }

    @Test
    @Disabled
    @DisplayName("relevancyEvaulator Test2")
    void WebControllerRelevenceEvaluatorWrongAnswer() {
        String question = "What is the capital of Japan?";
        // Ai response from the prompt method of WebController
        String aiResponse = webController.prompt(question);

        EvaluationRequest evaluationRequest = new EvaluationRequest(question, List.of(groundTruthDocument), aiResponse);
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

        Assertions.assertAll(() -> assertThat(aiResponse).isNotBlank(),
                () -> assertThat(evaluationResponse.isPass())
                        .withFailMessage(
                                "Expected the AI response to be relevant to the question, but it was not. " +
                                        "Question: %s, AI Response: %s".formatted(question, aiResponse))
                        .isFalse(),
                () -> assertThat(evaluationResponse.getScore())
                        .withFailMessage("Expected the relevancy score to be less than or equal to %s, but it was %s. " +
                                "Question: %s, AI Response: %s", minRelivenceScore, evaluationResponse.getScore(), question, aiResponse)
                        .isLessThan(minRelivenceScore)
        );
    }


    // FactChecking is response is not giving the correct answer so for now skip.
    @Test
    @DisplayName("FactCheckingEvaluatorTest1")
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void setFactCheckingEvaluator() {
        // Given
        String question = "What is the capital of Japan? Answer in one word";
        // Ai response from the prompt method of WebController
        String aiResponse = webController.chat(question);
        logger.info("AI response: " + aiResponse);
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, List.of(groundTruthDocument), aiResponse);
        logger.info("Evaluation getResponseContext :" + evaluationRequest.getResponseContent() + "Evaluation getUserText :" + evaluationRequest.getUserText());

        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
        logger.info("Evaluation response: " + evaluationResponse);


        Assertions.assertAll(() -> assertThat(aiResponse).isNotBlank(),
                () -> assertThat(evaluationResponse.isPass())
                        .withFailMessage(
                                "Expected the AI response to be relevant to the question, but it was not. " +
                                        "Question: %s, AI Response: %s".formatted(question, aiResponse))
                        .isTrue()
        );
    }

    @Test
    @Disabled
    @DisplayName("FactCheckingEvaluatorTest2")
    void setFactCheckingEvaluatorFailCheck() {
        String question = "What is the capital of Japan?";
        String aiResponse = webController.prompt(question);

        EvaluationRequest evaluationRequest = new EvaluationRequest(question, List.of(groundTruthDocument), aiResponse);
        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

        Assertions.assertAll(() -> assertThat(aiResponse).isNotBlank(),
                () -> assertThat(evaluationResponse.isPass())
                        .withFailMessage(
                                "Expected the AI response to be relevant to the question, but it was not. " +
                                        "Question: %s, AI Response: %s".formatted(question, aiResponse))
                        .isFalse()
        );
    }

    // FactCheckingEvaluator適切な実装
    @Test
    @DisplayName("FactCheckingEvaluatorTest3")
    void setFactCheckingEvaluatorProper() {
        // Given
        String question = "What is the capital of Japan? Answer in one word";
        // Ai response from the prompt method of WebController
        String aiResponse = webController.chat(question);
        logger.info("AI response: " + aiResponse);
        // Calling DataLoader
        logger.info("Dataloader checker:" + dataLoader.getJapanDoc());
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, dataLoader.getJapanDoc(), aiResponse);
        logger.info("Evaluation getResponseContext :" + evaluationRequest.getResponseContent() + "Evaluation getUserText :" + evaluationRequest.getUserText());
        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
        logger.info("Evaluation response: " + evaluationResponse);
        Assertions.assertAll(() -> assertThat(aiResponse).isNotBlank(),
                () -> assertThat(evaluationResponse.isPass())
                        .withFailMessage(
                                "Expected the AI response to be relevant to the question, but it was not. " +
                                        "Question: %s, AI Response: %s".formatted(question, aiResponse))
                        .isTrue()
        );
    }

    // FactCheckingEvaluator適切な実装
    @Test
    @DisplayName("AI Rag scenario checker")
    void evaluateHrPolicy() throws Exception {
        // Given
        String question = "What is the total  Vacation leave we can take?";
        // Ai response from the prompt method of WebController
        String aiResponse = webController.prompt(question);
        logger.info("AI response: " + aiResponse);

        String hrpolicy = HrPolicy.getContentAsString(StandardCharsets.UTF_8);

        // Calling DataLoader
        logger.info("Dataloader checker:" + hrpolicy);
        EvaluationRequest evaluationRequest = new EvaluationRequest(question, List.of(new Document(hrpolicy)), aiResponse);
        logger.info("Evaluation getResponseContext :" + evaluationRequest.getResponseContent() + "Evaluation getUserText :" + evaluationRequest.getUserText());
        EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);
        logger.info("Evaluation response: " + evaluationResponse);
        Assertions.assertAll(() -> assertThat(aiResponse).isNotBlank(),
                () -> assertThat(evaluationResponse.isPass())
                        .withFailMessage(
                                "Expected the AI response to be relevant to the question, but it was not. " +
                                        "Question: %s, AI Response: %s".formatted(question, aiResponse))
                        .isTrue()
        );
    }


}
