package com.doacoes.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doacoes.api.exceptions.MessageFeedbackException;
import com.doacoes.api.model.Campanha;
import com.doacoes.api.model.Doacao;
import com.doacoes.api.model.Usuario;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.CampanhaRepository;
import com.doacoes.api.repository.DoacaoRepository;
import com.doacoes.api.repository.UsuarioRepository;
import com.doacoes.api.security.services.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/doacoes")
@RequiredArgsConstructor
public class DoacaoController {

    private final DoacaoRepository doacaoRepository;
    private final CampanhaRepository campanhaRepository;
    private final UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<Doacao> registrarDoacao(@Valid @RequestBody Doacao doacao) {
        try {
            Optional<Campanha> campanhaOpt = campanhaRepository.findById(doacao.getCampanha().getId());
            if (!campanhaOpt.isPresent()) {
            	throw new MessageFeedbackException("Erro: Campanha não encontrada!", HttpStatus.NOT_FOUND);
            }
            
            Campanha campanha = campanhaOpt.get();
            
            if (campanha.getStatus() != Campanha.StatusCampanha.ATIVA) {
            	throw new MessageFeedbackException("Erro: Campanha não está ativa!", HttpStatus.BAD_REQUEST);
            }
            
            if (!doacao.isAnonimo() && doacao.getDoador() != null) {
                Optional<Usuario> doadorOpt = usuarioRepository.findById(doacao.getDoador().getId());
                if (!doadorOpt.isPresent()) {
                	throw new MessageFeedbackException("Erro: Doador não encontrado!", HttpStatus.NOT_FOUND);
                }
                doacao.setDoador(doadorOpt.get());
            }
            
            doacao.setCampanha(campanha);
            
            Doacao novaDoacao = doacaoRepository.save(doacao);
            
            if (doacao.getStatus() == Doacao.StatusDoacao.CONFIRMADA) {
                campanha.setValorArrecadado(campanha.getValorArrecadado().add(doacao.getValor()));
                campanhaRepository.save(campanha);
            }
            
            return ResponseEntity.ok(novaDoacao);
        } catch (Exception e) {
            throw new MessageFeedbackException("Erro ao registrar doação: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Doacao>> listarDoacoesPorUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (!usuarioOpt.isPresent()) {
        	throw new MessageFeedbackException("Erro: Usuário não encontrado!", HttpStatus.NOT_FOUND);
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            
            if (!userDetails.getId().equals(id) && 
                !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"))) {
            	
            	throw new MessageFeedbackException("Erro: Não autorizado!", HttpStatus.UNAUTHORIZED);
            }
        }
        
        List<Doacao> doacoes = doacaoRepository.findByDoadorId(id);
        return ResponseEntity.ok(doacoes);
    }

    @GetMapping("/campanha/{id}")
    public ResponseEntity<List<Doacao>> listarDoacoesPorCampanha(@PathVariable Long id) {
        Optional<Campanha> campanhaOpt = campanhaRepository.findById(id);
        if (!campanhaOpt.isPresent()) {
        	throw new MessageFeedbackException("Erro: Campanha não encontrada!", HttpStatus.NOT_FOUND);
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
            
            List<Doacao> doacoes = doacaoRepository.findByCampanhaId(id);
            doacoes.sort((a, b) -> b.getDataHora().compareTo(a.getDataHora()));
            
            if (isAdmin) {
                return ResponseEntity.ok(doacoes);
            } else {
                List<Doacao> doacoesLimitadas = doacoes.stream()
                    .limit(5)
                    .map(doacao -> {
                        Doacao doacaoLimitada = new Doacao();
                        doacaoLimitada.setDataHora(doacao.getDataHora());
                        doacaoLimitada.setValor(doacao.getValor());
                        doacaoLimitada.setAnonimo(doacao.isAnonimo());
                        if (!doacao.isAnonimo() && doacao.getDoador() != null) {
                            Usuario doadorLimitado = new Usuario();
                            doadorLimitado.setNome(doacao.getDoador().getNome());
                            doacaoLimitada.setDoador(doadorLimitado);
                        }
                        return doacaoLimitada;
                    })
                    .collect(Collectors.toList());
                return ResponseEntity.ok(doacoesLimitadas);
            }
        } else {
            List<Doacao> doacoes = doacaoRepository.findByCampanhaId(id);
            doacoes.sort((a, b) -> b.getDataHora().compareTo(a.getDataHora()));
            List<Doacao> doacoesLimitadas = doacoes.stream()
                .limit(5)
                .map(doacao -> {
                    Doacao doacaoLimitada = new Doacao();
                    doacaoLimitada.setDataHora(doacao.getDataHora());
                    doacaoLimitada.setValor(doacao.getValor());
                    doacaoLimitada.setAnonimo(doacao.isAnonimo());
                    if (!doacao.isAnonimo() && doacao.getDoador() != null) {
                        Usuario doadorLimitado = new Usuario();
                        doadorLimitado.setNome(doacao.getDoador().getNome());
                        doacaoLimitada.setDoador(doadorLimitado);
                    }
                    return doacaoLimitada;
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(doacoesLimitadas);
        }
    
    }
    
    @GetMapping("/usuario/atual")
	@PreAuthorize("hasRole('DOADOR') or hasRole('ADMINISTRADOR')")
	public ResponseEntity<List<Doacao>> listarDoacoesUsuarioAtual(Authentication authentication) {
	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	    Long userId = userDetails.getId();
	    
	    List<Doacao> doacoes = doacaoRepository.findByDoadorId(userId);
	    return ResponseEntity.ok(doacoes);
	}

    @PutMapping("/{id}/confirmar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MessageResponse> confirmarDoacao(@PathVariable Long id) {
        Optional<Doacao> doacaoOpt = doacaoRepository.findById(id);
        if (!doacaoOpt.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro: Doação não encontrada!"));
        }
        
        Doacao doacao = doacaoOpt.get();
        
        if (doacao.getStatus() == Doacao.StatusDoacao.CONFIRMADA) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro: Doação já está confirmada!"));
        }
        
        doacao.setStatus(Doacao.StatusDoacao.CONFIRMADA);
        doacaoRepository.save(doacao);
        
        Campanha campanha = doacao.getCampanha();
        campanha.setValorArrecadado(campanha.getValorArrecadado().add(doacao.getValor()));
        campanhaRepository.save(campanha);
        
        return ResponseEntity.ok(new MessageResponse("Doação confirmada com sucesso!"));
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MessageResponse> cancelarDoacao(@PathVariable Long id) {
        Optional<Doacao> doacaoOpt = doacaoRepository.findById(id);
        if (!doacaoOpt.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro: Doação não encontrada!"));
        }
        
        Doacao doacao = doacaoOpt.get();
        
        if (doacao.getStatus() == Doacao.StatusDoacao.CANCELADA) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro: Doação já está cancelada!"));
        }
        
        if (doacao.getStatus() == Doacao.StatusDoacao.CONFIRMADA) {
            Campanha campanha = doacao.getCampanha();
            campanha.setValorArrecadado(campanha.getValorArrecadado().subtract(doacao.getValor()));
            campanhaRepository.save(campanha);
        }
        
        doacao.setStatus(Doacao.StatusDoacao.CANCELADA);
        doacaoRepository.save(doacao);
        
        return ResponseEntity.ok(new MessageResponse("Doação cancelada com sucesso!"));
    }
}
