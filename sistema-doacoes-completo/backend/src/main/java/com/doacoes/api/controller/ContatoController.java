package com.doacoes.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doacoes.api.exceptions.MessageFeedbackException;
import com.doacoes.api.model.MensagemContato;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.MensagemContatoRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contato")
@RequiredArgsConstructor
public class ContatoController {

    private final MensagemContatoRepository repo;

    @PostMapping("/enviar")
    public ResponseEntity<?> enviarMensagem(@Valid @RequestBody MensagemContato mensagem) {
        // basic validation
        if ((mensagem.getTitulo() == null || mensagem.getTitulo().isBlank()) &&
                (mensagem.getDescricao() == null || mensagem.getDescricao().isBlank())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Título ou descrição é obrigatório"));
        }

        MensagemContato saved = repo.save(mensagem);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> listarEmailsContato() {
        // static list for now, could come from config
        List<String> emails = List.of("suporte@doacoes.local", "contato@doacoes.local");
        return ResponseEntity.ok(emails);
    }

    // Admin CRUD
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<MensagemContato>> listarTodas() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> obter(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok)
        		.orElseThrow(() -> new MessageFeedbackException("Mensagem não encontrada"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody MensagemContato m) {
        return repo.findById(id).map(existing -> {
            existing.setTitulo(m.getTitulo());
            existing.setDescricao(m.getDescricao());
            existing.setEmail(m.getEmail());
            repo.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseThrow(() -> new MessageFeedbackException("Mensagem não encontrada"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Mensagem não encontrada"));
        }
        repo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Mensagem removida"));
    }

}
