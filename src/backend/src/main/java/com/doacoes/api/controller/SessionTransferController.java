package com.doacoes.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doacoes.api.payload.form.SessionExchangeRequest;
import com.doacoes.api.payload.form.SessionTransferRequest;
import com.doacoes.api.payload.response.JwtResponse;
import com.doacoes.api.payload.response.SessionTransferResponse;
import com.doacoes.api.security.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SessionTransferController {

	private final AuthService authService;
	
	/**
     * Gera um token temporário para transferir a sessão
     * Requer usuário autenticado
     */
    @PostMapping("/session-transfer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SessionTransferResponse> generateSessionTransferToken(@Valid @RequestBody SessionTransferRequest request) {

        String token = authService.createSessionTransferToken(request.getTarget());

        SessionTransferResponse response = new SessionTransferResponse();
        response.setTransferToken(token);

        return ResponseEntity.ok(response);
    }

    /**
     * Troca o token temporário por um JWT válido
     */
    @PostMapping("/session-exchange")
    public ResponseEntity<JwtResponse> exchangeSession(@RequestBody SessionExchangeRequest request) {

        JwtResponse jwtResponse = authService.exchangeSession(request.getToken());

        return ResponseEntity.ok(jwtResponse);
    }
	
}
