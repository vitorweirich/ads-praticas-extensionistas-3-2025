package com.doacoes.api.controller;

import com.doacoes.api.model.Campanha;
import com.doacoes.api.model.Usuario;
import com.doacoes.api.repository.CampanhaRepository;
import com.doacoes.api.repository.UsuarioRepository;
import com.doacoes.api.security.services.UserDetailsImpl;
import com.doacoes.api.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/campanhas")
public class CampanhaController {

    @Autowired
    private CampanhaRepository campanhaRepository;
    
    // TODO: Trocar @Autowired por injeção por construtor
    @Autowired
    private UsuarioRepository usuarioRepository;

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
