package com.doacoes.api.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
	
	@NotBlank
    @Size(max = 100)
    @Email
	private String email;
	
}
