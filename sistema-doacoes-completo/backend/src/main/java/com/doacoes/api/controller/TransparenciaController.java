package com.doacoes.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doacoes.api.exceptions.MessageFeedbackException;
import com.doacoes.api.model.Campanha;
import com.doacoes.api.model.Transparencia;
import com.doacoes.api.model.Usuario;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.CampanhaRepository;
import com.doacoes.api.repository.TransparenciaRepository;
import com.doacoes.api.repository.UsuarioRepository;
import com.doacoes.api.security.services.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transparencia")
@RequiredArgsConstructor
public class TransparenciaController {

    private final TransparenciaRepository transparenciaRepository;
    private final CampanhaRepository campanhaRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/publica")
    public ResponseEntity<List<Transparencia>> listarAlocacoesPublicas() {
        List<Transparencia> alocacoes = transparenciaRepository.findAll();
        return ResponseEntity.ok(alocacoes);
    }

    @GetMapping("/campanha/{id}")
    public ResponseEntity<List<Transparencia>> listarAlocacoesPorCampanha(@PathVariable Long id) {
        Optional<Campanha> campanhaOpt = campanhaRepository.findById(id);
        if (!campanhaOpt.isPresent()) {
        	throw new MessageFeedbackException("Erro: Campanha não encontrada!", HttpStatus.NOT_FOUND);
        }
        
        List<Transparencia> alocacoes = transparenciaRepository.findByCampanhaId(id);
        return ResponseEntity.ok(alocacoes);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Transparencia> registrarAlocacao(@Valid @RequestBody Transparencia alocacao) {
        try {
            Optional<Campanha> campanhaOpt = campanhaRepository.findById(alocacao.getCampanha().getId());
            if (!campanhaOpt.isPresent()) {
            	throw new MessageFeedbackException("Erro: Campanha não encontrada!", HttpStatus.NOT_FOUND);
            }
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            
            Optional<Usuario> responsavelOpt = usuarioRepository.findById(userDetails.getId());
            if (!responsavelOpt.isPresent()) {
            	throw new MessageFeedbackException("Erro: Responsável não encontrado!", HttpStatus.NOT_FOUND);
            }
            
            alocacao.setCampanha(campanhaOpt.get());
            alocacao.setResponsavel(responsavelOpt.get());
            
            Transparencia novaAlocacao = transparenciaRepository.save(alocacao);
            
            return ResponseEntity.ok(novaAlocacao);
        } catch (Exception e) {
        	throw new MessageFeedbackException("Erro ao registrar alocação: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Transparencia> atualizarAlocacao(@PathVariable Long id, @Valid @RequestBody Transparencia alocacaoAtualizada) {
        Optional<Transparencia> alocacaoOpt = transparenciaRepository.findById(id);
        
        if (alocacaoOpt.isPresent()) {
            Transparencia alocacao = alocacaoOpt.get();
            
            if (alocacaoAtualizada.getCampanha() != null && alocacaoAtualizada.getCampanha().getId() != null) {
                Optional<Campanha> campanhaOpt = campanhaRepository.findById(alocacaoAtualizada.getCampanha().getId());
                if (!campanhaOpt.isPresent()) {
                	throw new MessageFeedbackException("Erro: Campanha não encontrada!", HttpStatus.NOT_FOUND);
                }
                alocacao.setCampanha(campanhaOpt.get());
            }
            
            alocacao.setTituloAlocacao(alocacaoAtualizada.getTituloAlocacao());
            alocacao.setDescricaoAlocacao(alocacaoAtualizada.getDescricaoAlocacao());
            alocacao.setValorAlocado(alocacaoAtualizada.getValorAlocado());
            alocacao.setComprovante(alocacaoAtualizada.getComprovante());
            
            return ResponseEntity.ok(transparenciaRepository.save(alocacao));
        } else {
        	throw new MessageFeedbackException("Erro: Alocação não encontrada!", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MessageResponse> excluirAlocacao(@PathVariable Long id) {
        try {
            transparenciaRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Alocação excluída com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro ao excluir alocação: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transparencia> obterAlocacao(@PathVariable Long id) {
        Optional<Transparencia> alocacao = transparenciaRepository.findById(id);
        if (alocacao.isPresent()) {
            return ResponseEntity.ok(alocacao.get());
        } else {
        	throw new MessageFeedbackException("Erro: Alocação não encontrada!", HttpStatus.NOT_FOUND);
        }
    }
}
