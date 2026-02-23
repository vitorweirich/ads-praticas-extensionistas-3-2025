package com.doacoes.api.security.jwt;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.doacoes.api.config.JwtProperties;
import com.doacoes.api.constants.JwtClaims;
import com.doacoes.api.security.services.UserDetailsImpl;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private SecretKey jwtSecret;

    private int jwtExpirationSeconds;
    
    public JwtUtils(JwtProperties jwtProperties) {
        super();
        this.jwtSecret = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.jwtExpirationSeconds = jwtProperties.getExpiration();
    }

    public String generateJwtToken(Authentication authentication) {
        return this.generateJwtToken((UserDetailsImpl) authentication.getPrincipal());
    }
    
    public String generateJwtToken(UserDetailsImpl userPrincipal) {
    	List<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
    	
    	return Jwts.builder()
    			.subject(userPrincipal.getUsername())
    			.claim(JwtClaims.ROLES, roles)
    			.claim(JwtClaims.USER_NAME, userPrincipal.getUsername())
    			.issuedAt(new Date())
    			.expiration(new Date(System.currentTimeMillis() + (jwtExpirationSeconds * 1000)))
    			.signWith(jwtSecret, Jwts.SIG.HS512)
    			.compact();
    }

    public String getUserNameFromJwtToken(String token) {
    	return Jwts.parser()
    			.verifyWith(jwtSecret)
    			.build()
    			.parseSignedClaims(token)
    			.getPayload().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(jwtSecret).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        }
        return false;
    }
}
