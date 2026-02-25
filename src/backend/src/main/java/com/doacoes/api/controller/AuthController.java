package com.doacoes.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doacoes.api.model.Usuario;
import com.doacoes.api.payload.request.ForgotPasswordRequest;
import com.doacoes.api.payload.request.LoginRequest;
import com.doacoes.api.payload.request.ResetPasswordRequest;
import com.doacoes.api.payload.request.SignupRequest;
import com.doacoes.api.payload.response.JwtResponse;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.UsuarioRepository;
import com.doacoes.api.security.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = this.authService.authenticateUser(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro: Email já está em uso!"));
        }

        Usuario usuario = new Usuario();
        usuario.setNome(signUpRequest.getNome());
        usuario.setEmail(signUpRequest.getEmail());
        usuario.setSenha(encoder.encode(signUpRequest.getSenha()));
        usuario.setTipo(Usuario.TipoUsuario.DOADOR);
        usuario.setEndereco(signUpRequest.getEndereco());
        usuario.setTelefone(signUpRequest.getTelefone());
        usuario.setCpfCnpj(signUpRequest.getCpfCnpj());

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new MessageResponse("Usuário registrado com sucesso!"));
    }
    
    @PostMapping("/cadastro-admin")
    public ResponseEntity<MessageResponse> registerAdminUser(@Valid @RequestBody SignupRequest signUpRequest) {
    	if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
    		return ResponseEntity
    				.badRequest()
    				.body(new MessageResponse("Erro: Email já está em uso!"));
    	}
    	
    	Usuario usuario = new Usuario();
    	usuario.setNome(signUpRequest.getNome());
    	usuario.setEmail(signUpRequest.getEmail());
    	usuario.setSenha(encoder.encode(signUpRequest.getSenha()));
    	usuario.setTipo(Usuario.TipoUsuario.ADMINISTRADOR);
    	usuario.setEndereco(signUpRequest.getEndereco());
    	usuario.setTelefone(signUpRequest.getTelefone());
    	usuario.setCpfCnpj(signUpRequest.getCpfCnpj());
    	
    	usuarioRepository.save(usuario);
    	
    	return ResponseEntity.ok(new MessageResponse("Usuário registrado com sucesso!"));
    }
    
    @PostMapping("/esqueci-senha")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
        	authService.generateTokenAndSendEmail(request);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/resetar-senha")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
    	authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
