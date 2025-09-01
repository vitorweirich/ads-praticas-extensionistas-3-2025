package com.doacoes.api.exceptions;

@SuppressWarnings("serial")
public class MessageFeedbackException extends RuntimeException {
    public MessageFeedbackException(String message) {
        super(message);
    }
    
    public MessageFeedbackException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
