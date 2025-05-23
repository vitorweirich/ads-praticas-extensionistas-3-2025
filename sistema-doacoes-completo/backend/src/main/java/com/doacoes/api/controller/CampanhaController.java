package com.doacoes.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import com.doacoes.api.model.Campanha;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.CampanhaRepository;
import com.doacoes.api.repository.UsuarioRepository;
import com.doacoes.api.security.services.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/campanhas")
@RequiredArgsConstructor
public class CampanhaController {

    private final CampanhaRepository campanhaRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/publicas")
    public ResponseEntity<List<Campanha>> listarCampanhasPublicas() {
        List<Campanha> campanhas = campanhaRepository.findByStatus(Campanha.StatusCampanha.ATIVA);
        return ResponseEntity.ok(campanhas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterCampanha(@PathVariable Long id) {
        Optional<Campanha> campanha = campanhaRepository.findById(id);
        if (campanha.isPresent()) {
            return ResponseEntity.ok(campanha.get());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro: Campanha não encontrada!"));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    // TODO: Criar exception handler para o @Valid (geral)
    public ResponseEntity<?> criarCampanha(@Valid @RequestBody Campanha campanha) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl admUser = (UserDetailsImpl) authentication.getPrincipal();
            campanha.setAdministrador(usuarioRepository.getReferenceById(admUser.getId()));          
            Campanha novaCampanha = campanhaRepository.save(campanha);
            return ResponseEntity.ok(novaCampanha);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro ao criar campanha: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> atualizarCampanha(@PathVariable Long id, @Valid @RequestBody Campanha campanhaAtualizada) {
        Optional<Campanha> campanhaData = campanhaRepository.findById(id);
        
        if (campanhaData.isPresent()) {
            Campanha campanha = campanhaData.get();
            campanha.setTitulo(campanhaAtualizada.getTitulo());
            campanha.setDescricao(campanhaAtualizada.getDescricao());
            campanha.setImagemCapa(campanhaAtualizada.getImagemCapa());
            campanha.setMetaFinanceira(campanhaAtualizada.getMetaFinanceira());
            campanha.setDataTermino(campanhaAtualizada.getDataTermino());
            campanha.setStatus(campanhaAtualizada.getStatus());
            campanha.setCategoria(campanhaAtualizada.getCategoria());
            campanha.setBeneficiarios(campanhaAtualizada.getBeneficiarios());
            campanha.setGaleriaImagens(campanhaAtualizada.getGaleriaImagens());
            
            return ResponseEntity.ok(campanhaRepository.save(campanha));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro: Campanha não encontrada!"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> excluirCampanha(@PathVariable Long id) {
        try {
            campanhaRepository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Campanha excluída com sucesso!"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro ao excluir campanha: " + e.getMessage()));
        }
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Campanha>> listarCampanhasPorCategoria(@PathVariable String categoria) {
        List<Campanha> campanhas = campanhaRepository.findByCategoria(categoria);
        return ResponseEntity.ok(campanhas);
    }
}
