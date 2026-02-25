package com.doacoes.api.security.services;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doacoes.api.exceptions.MessageFeedbackException;
import com.doacoes.api.model.PasswordResetTokenEntity;
import com.doacoes.api.model.SessionTransferTokenEntity;
import com.doacoes.api.model.Usuario;
import com.doacoes.api.payload.request.ForgotPasswordRequest;
import com.doacoes.api.payload.request.LoginRequest;
import com.doacoes.api.payload.request.ResetPasswordRequest;
import com.doacoes.api.payload.response.JwtResponse;
import com.doacoes.api.repository.PasswordResetTokenRepository;
import com.doacoes.api.repository.SessionTransferTokenRepository;
import com.doacoes.api.security.jwt.JwtUtils;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final AuthenticationManager authenticationManager;
	private final UserDetailsServiceImpl userDetailsService;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final SessionTransferTokenRepository sessionTransferTokenRepository;
	private final EmailService emailService;
	private final PasswordEncoder encoder;
	private final JwtUtils jwtUtils;
	
	@Value("${app.reset-password.base-url:http://localhost:3000/resetar-senha}")
    private String resetPasswordBaseUrl;
	@Value("${app.reset-password.expiration-minutes:30}")
    private long resetPasswordExpirationMinutes;
	
	public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                 userDetails.getId(),
                 userDetails.getNome(),
                 userDetails.getEmail(),
                 roles);
    }

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
    
    @Transactional
    public String createSessionTransferToken(String target) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MessageFeedbackException("Não autenticado", HttpStatus.UNAUTHORIZED);
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Instant now = Instant.now();

        SessionTransferTokenEntity token = new SessionTransferTokenEntity();
        token.setId(UUID.randomUUID());
        token.setUser(userDetailsService.getReferenceById(userDetails.getId()));
        token.setCreatedAt(now);
        token.setExpiresAt(now.plus(Duration.ofMinutes(2)));
        token.setUsed(false);
        token.setTarget(target);

        sessionTransferTokenRepository.save(token);

        return token.getId().toString();
    }
    
    @Transactional
    public JwtResponse exchangeSession(String transferToken) {
        SessionTransferTokenEntity token = sessionTransferTokenRepository
                .findById(UUID.fromString(transferToken))
                .orElseThrow(() -> 
                    new MessageFeedbackException("Token inválido", HttpStatus.UNAUTHORIZED)
                );

        // TODO: Criar scheduler para remover tokens espirados e usados após um delay
        if (token.isUsed() || token.getExpiresAt().isBefore(Instant.now())) {
            throw new MessageFeedbackException("Token inválido ou expirado", HttpStatus.UNAUTHORIZED);
        }

        token.setUsed(true);
        sessionTransferTokenRepository.save(token);

        Usuario user = token.getUser();

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(user.getEmail());
        String jwt = jwtUtils.generateJwtToken(userDetails);


        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getNome(),
                userDetails.getEmail(),
                roles
        );
    }
	
}
