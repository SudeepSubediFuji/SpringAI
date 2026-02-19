CREATE TABLE SPRING_AI (
                                       conversation_id VARCHAR(36) NOT NULL,
                                       content LONGVARCHAR NOT NULL,
                                       type VARCHAR(10) NOT NULL,
                                       "timestamp" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX SPRING_AI_CONVERSATION_ID_TIMESTAMP_IDX ON SPRING_AI(conversation_id, "timestamp" DESC);

ALTER TABLE SPRING_AIADD CONSTRAINT TYPE_CHECK CHECK (type IN ('USER', 'ASSISTANT', 'SYSTEM', 'TOOL'));
