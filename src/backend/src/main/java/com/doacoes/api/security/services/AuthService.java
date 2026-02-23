package com.doacoes.api.security.services;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doacoes.api.exceptions.MessageFeedbackException;
import com.doacoes.api.model.PasswordResetTokenEntity;
import com.doacoes.api.model.Usuario;
import com.doacoes.api.payload.request.ForgotPasswordRequest;
import com.doacoes.api.payload.request.ResetPasswordRequest;
import com.doacoes.api.repository.PasswordResetTokenRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserDetailsServiceImpl userDetailsService;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final EmailService emailService;
	private final PasswordEncoder encoder;
	
	@Value("${app.reset-password.base-url:http://localhost:3000/resetar-senha}")
    private String resetPasswordBaseUrl;
	@Value("${app.reset-password.expiration-minutes:30}")
    private long resetPasswordExpirationMinutes;

	@Transactional
	public void generateTokenAndSendEmail(ForgotPasswordRequest request) throws MessagingException, IOException {
		Usuario user = userDetailsService.findByEmail(request.getEmail());
        
        Instant now = Instant.now();

        PasswordResetTokenEntity token = new PasswordResetTokenEntity();
        token.setId(UUID.randomUUID());
        token.setUser(user);
        token.setCreatedAt(now);
        token.setExpiresAt(now.plus(Duration.ofMinutes(resetPasswordExpirationMinutes)));
        passwordResetTokenRepository.save(token);

        String link = resetPasswordBaseUrl + "/" + token.getId();
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getNome());
        variables.put("resetLink", link);
        emailService.sendHtmlEmailFromTemplate(user.getEmail(), "Redefina sua senha", "reset-password-email", variables);
    }
	
	@Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetTokenEntity token = passwordResetTokenRepository.findById(UUID.fromString(request.getToken()))
                .orElseThrow(() -> new MessageFeedbackException("Token inválido ou já usado", HttpStatus.UNAUTHORIZED));

        if (isResetTokenExpired(token)) {
            throw new MessageFeedbackException("Token inválido ou já usado", HttpStatus.UNAUTHORIZED);
        }

        Usuario user = token.getUser();
        user.setSenha(encoder.encode(request.getNewPassword()));

        userDetailsService.save(user);
        passwordResetTokenRepository.delete(token);
    }
    
    private boolean isResetTokenExpired(PasswordResetTokenEntity token) {
        return token.getExpiresAt().isBefore(Instant.now());
    }
	
}
