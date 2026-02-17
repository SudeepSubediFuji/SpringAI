package com.fuji.Evaluator.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.beans.factory.annotation.Qualifier;

public class CustomFactCheckingEvalutor extends FactCheckingEvaluator {

    public CustomFactCheckingEvalutor(@Qualifier("openAiBuilder") ChatClient.Builder chatClientBuilder) {
        super(chatClientBuilder,"""
			Evaluate whether or not the following claim is supported by the provided document.
				Respond with "yes" if the claim is supported, or "no" if it is not.

				Document:
				{document}

				Claim:
				{claim}
			""");
    }
}
