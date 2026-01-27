package com.chatAI.multiChatClient.service;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TokenService {

    private final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();

    // GPT-4o and GPT-4o-mini use the o200k_base encoding
    private final Encoding encoding = registry.getEncodingForModel(ModelType.GPT_4O);

    Logger logger = Logger.getLogger(TokenService.class.getName());

    public int countTokens(String text) {
        if (text == null) return 0;
        return encoding.countTokens(text);
    }

    public int calculateMaxTokens(String prompt, int contextWindow) {
        int usedTokens = countTokens(prompt);
        int safetyBuffer = 1000;

        // 128,000 - used - buffer
        int remaining = contextWindow - usedTokens - safetyBuffer;
        logger.info("Used Tokens(prompt): " + usedTokens +"\n"+ ", Remaining Tokens(contextWindow - usedTokens - safetyBuffer): " + remaining);

        // Ensure we don't return a negative number and
        // cap it at the model's actual output limit (e.g., 4096 or 16384)
        return Math.max(0, Math.min(remaining, 4096));
    }
}
