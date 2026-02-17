package com.fuji.Evaluator.exception;

public class InvalidAnswerException extends RuntimeException{
    public InvalidAnswerException(String question, String answer){
        super("Answer Check failed: "+"\n"+"The answer:"+answer+"\n"+"Please check the question: "+question+"\n");
    }
}
