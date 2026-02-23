package com.doacoes.api.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse("INTERNAL_ERROR", "Erro interno do servidor"));
	}
	
	@ExceptionHandler(MessageFeedbackException.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(MessageFeedbackException ex) {
		ErrorResponse exceptionResponse = new ErrorResponse(ex.getStatus().getReasonPhrase(), ex.getMessage());
		
		return ResponseEntity.status(ex.getStatus())
				.body(exceptionResponse);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<String> errors = new ArrayList<>();
		fieldErrors.forEach(f -> errors.add(String.format("%s : %s", f.getField(), f.getDefaultMessage())));

		ErrorResponse exceptionResponse = new ErrorResponse("VALIDATION_FAILED", "Erro de validação", errors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("INVALID_ARGUMENT", ex.getMessage()));
	}

}
