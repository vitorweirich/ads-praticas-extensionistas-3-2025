package com.doacoes.api.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doacoes.api.model.Usuario;
import com.doacoes.api.payload.form.AlterarUsuarioForm;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.UsuarioRepository;
import com.doacoes.api.security.services.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
	
	private final UsuarioRepository usuarioRepository;
	
	private final PasswordEncoder encoder;

	@GetMapping
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> listarUsuarios() {
		List<Usuario> allUsers = usuarioRepository.findAll();
		
		return ResponseEntity.ok(allUsers);
	}
	
	@GetMapping("/atual")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> buscarUsuarioAtual(Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	    Long userId = userDetails.getId();
		return ResponseEntity.ok(usuarioRepository.findById(userId).orElseThrow());
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR') or @userSecurity.isCurrentUser(#id)")
	public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody AlterarUsuarioForm usuarioAtualizado) {
	    Optional<Usuario> usuarioData = usuarioRepository.findById(id);
	    
	    if (usuarioData.isPresent()) {
	        Usuario usuario = usuarioData.get();
	        usuario.setNome(usuarioAtualizado.getNome());
	        usuario.setEmail(usuarioAtualizado.getEmail());
	        usuario.setTelefone(usuarioAtualizado.getTelefone());
	        usuario.setEndereco(usuarioAtualizado.getEndereco());
	        
	        return ResponseEntity.ok(usuarioRepository.save(usuario));
	    } else {
	        return ResponseEntity
	                .badRequest()
	                .body(new MessageResponse("Erro: Usuário não encontrado!"));
	    }
	}

	@PutMapping("/{id}/senha")
	@PreAuthorize("hasRole('ADMINISTRADOR') or @userSecurity.isCurrentUser(#id)")
	public ResponseEntity<?> alterarSenha(@PathVariable Long id, @Valid @RequestBody Map<String, String> senhas) {
	    Optional<Usuario> usuarioData = usuarioRepository.findById(id);
	    
	    if (usuarioData.isPresent()) {
	        Usuario usuario = usuarioData.get();
	        
	        if (!encoder.matches(senhas.get("senhaAtual"), usuario.getSenha())) {
	            return ResponseEntity
	                    .badRequest()
	                    .body(new MessageResponse("Erro: Senha atual incorreta!"));
	        }
	        
	        usuario.setSenha(encoder.encode(senhas.get("novaSenha")));
	        usuarioRepository.save(usuario);
	        
	        return ResponseEntity.ok(new MessageResponse("Senha alterada com sucesso!"));
	    } else {
	        return ResponseEntity
	                .badRequest()
	                .body(new MessageResponse("Erro: Usuário não encontrado!"));
	    }
	}

}
