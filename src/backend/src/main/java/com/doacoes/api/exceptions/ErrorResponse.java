package com.doacoes.api.exceptions;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private List<String> details;
	public ErrorResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

}
