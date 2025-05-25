package com.doacoes.api.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doacoes.api.model.Campanha;
import com.doacoes.api.model.Doacao;
import com.doacoes.api.model.Usuario;
import com.doacoes.api.payload.request.SignupRequest;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.CampanhaRepository;
import com.doacoes.api.repository.DoacaoRepository;
import com.doacoes.api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdministracaoController {

	private final CampanhaRepository campanhaRepository;
	private final DoacaoRepository doacaoRepository;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder encoder;
	
	@GetMapping("/estatisticas")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> getEstatisticas() {
	    Map<String, Object> estatisticas = new HashMap<>();
	    
	    long campanhasAtivas = campanhaRepository.countByStatus(Campanha.StatusCampanha.ATIVA);
	    estatisticas.put("campanhasAtivas", campanhasAtivas);
	    
	    BigDecimal totalArrecadado = doacaoRepository.sumValorByStatus(Doacao.StatusDoacao.CONFIRMADA);
	    estatisticas.put("totalArrecadado", totalArrecadado != null ? totalArrecadado : BigDecimal.ZERO);
	    
	    long totalDoacoes = doacaoRepository.count();
	    estatisticas.put("totalDoacoes", totalDoacoes);
	    
	    long totalUsuarios = usuarioRepository.count();
	    estatisticas.put("totalUsuarios", totalUsuarios);
	    
	    return ResponseEntity.ok(estatisticas);
	}
	
	// TODO: Criar DTO para não mostrar dados do usuario
	@GetMapping("/doacoes")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<List<Doacao>> listarTodasDoacoes() {
	    List<Doacao> doacoes = doacaoRepository.findAll();
	    return ResponseEntity.ok(doacoes);
	}
	
	@PostMapping("/auth/cadastro-admin")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> cadastrarAdmin(@Valid @RequestBody SignupRequest signUpRequest) {
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
	    
	    return ResponseEntity.ok(new MessageResponse("Administrador registrado com sucesso!"));
	}
	
	@PutMapping("/usuarios/{id}/status")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> alterarStatusUsuario(@PathVariable Long id, @RequestBody Map<String, Boolean> status) {
	    Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
	    
	    if (usuarioOpt.isPresent()) {
	        Usuario usuario = usuarioOpt.get();
	        usuario.setAtivo(status.get("ativo"));
	        usuarioRepository.save(usuario);
	        
	        return ResponseEntity.ok(new MessageResponse("Status do usuário alterado com sucesso!"));
	    } else {
	        return ResponseEntity
	                .badRequest()
	                .body(new MessageResponse("Erro: Usuário não encontrado!"));
	    }
	}

}
