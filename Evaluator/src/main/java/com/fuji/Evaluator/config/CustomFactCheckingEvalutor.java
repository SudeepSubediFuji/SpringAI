package com.fuji.Evaluator.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;

public class CustomFactCheckingEvalutor extends FactCheckingEvaluator {

    public CustomFactCheckingEvalutor(ChatClient.Builder chatClientBuilder) {
        super(chatClientBuilder,"""
				Evaluate whether or not the following claim is supported by the provided context.
				Respond with "yes" if the claim is supported, or "no" if it is not.

				Context:
				{context}

				Claim:
				{claim}
			""");
    }
}
