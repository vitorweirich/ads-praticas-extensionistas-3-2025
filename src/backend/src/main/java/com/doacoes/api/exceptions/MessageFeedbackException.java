package com.doacoes.api.exceptions;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class MessageFeedbackException extends RuntimeException {
	
	private final HttpStatus status;
	
    public MessageFeedbackException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
    public MessageFeedbackException(String message, HttpStatus status, Throwable throwable) {
        super(message, throwable);
        this.status = status;
    }
    
    public HttpStatus getStatus() {
    	return this.status;
    }
}
