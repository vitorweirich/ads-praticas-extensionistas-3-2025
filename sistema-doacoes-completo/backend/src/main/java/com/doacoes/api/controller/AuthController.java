package com.doacoes.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doacoes.api.model.Usuario;
import com.doacoes.api.payload.request.LoginRequest;
import com.doacoes.api.payload.request.SignupRequest;
import com.doacoes.api.payload.response.JwtResponse;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.UsuarioRepository;
import com.doacoes.api.security.jwt.JwtUtils;
import com.doacoes.api.security.services.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                                                 userDetails.getId(),
                                                 userDetails.getNome(),
                                                 userDetails.getEmail(),
                                                 roles));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
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
    public ResponseEntity<?> registerAdminUser(@Valid @RequestBody SignupRequest signUpRequest) {
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
}
