package com.doacoes.api.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.doacoes.api.model.Campanha;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.CampanhaRepository;
import com.doacoes.api.repository.DoacaoRepository;
import com.doacoes.api.repository.TransparenciaRepository;
import com.doacoes.api.repository.UsuarioRepository;
import com.doacoes.api.security.services.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/campanhas")
@RequiredArgsConstructor
public class CampanhaController {

    private final CampanhaRepository campanhaRepository;
    private final DoacaoRepository doacaoRepository;
    private final TransparenciaRepository transparenciaRepository;
    private final UsuarioRepository usuarioRepository;
    @Value("${app.base-url:http://localhost:8080}")
    private String appBaseUrl;

    @GetMapping("/publicas")
    public ResponseEntity<List<Campanha>> listarCampanhasPublicas() {
        List<Campanha> campanhas = campanhaRepository.findByStatus(Campanha.StatusCampanha.ATIVA);
        return ResponseEntity.ok(campanhas);
    }
    
    @GetMapping()
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<Campanha>> listarCampanhas() {
    	List<Campanha> campanhas = campanhaRepository.findAll();
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

    // Suporta multipart: campo 'campanha' com JSON e 'imagem' opcional com arquivo
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> criarCampanhaMultipart(@Valid @RequestPart("campanha") Campanha campanha,
            @RequestPart(value = "imagem", required = false) MultipartFile imagemFile) {
        try {
            if (imagemFile != null && !imagemFile.isEmpty()) {
                String fileUrl = storeImageAndGetUrl(imagemFile);
                campanha.setImagemCapa(fileUrl);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl admUser = (UserDetailsImpl) authentication.getPrincipal();
            campanha.setAdministrador(usuarioRepository.getReferenceById(admUser.getId()));

            Campanha novaCampanha = campanhaRepository.save(campanha);
            return ResponseEntity.ok(novaCampanha);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro ao criar campanha (multipart): " + e.getMessage()));
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
            
            return ResponseEntity.ok(campanhaRepository.save(campanha));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro: Campanha não encontrada!"));
        }
    }

    // Suporta multipart update
    @PutMapping(path = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> atualizarCampanhaMultipart(@PathVariable Long id,
            @RequestPart("campanha") Campanha campanhaAtualizada,
            @RequestPart(value = "imagem", required = false) MultipartFile imagemFile) {
        try {
            Optional<Campanha> campanhaData = campanhaRepository.findById(id);
            if (!campanhaData.isPresent()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Erro: Campanha não encontrada!"));
            }

            Campanha campanha = campanhaData.get();

            campanha.setTitulo(campanhaAtualizada.getTitulo());
            campanha.setDescricao(campanhaAtualizada.getDescricao());

            if (imagemFile != null && !imagemFile.isEmpty()) {
                String fileUrl = storeImageAndGetUrl(imagemFile);
                campanha.setImagemCapa(fileUrl);
            } else {
                campanha.setImagemCapa(campanhaAtualizada.getImagemCapa());
            }

            campanha.setMetaFinanceira(campanhaAtualizada.getMetaFinanceira());
            campanha.setDataTermino(campanhaAtualizada.getDataTermino());
            campanha.setStatus(campanhaAtualizada.getStatus());
            campanha.setCategoria(campanhaAtualizada.getCategoria());
            campanha.setBeneficiarios(campanhaAtualizada.getBeneficiarios());

            return ResponseEntity.ok(campanhaRepository.save(campanha));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erro ao atualizar campanha (multipart): " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Transactional
    public ResponseEntity<?> excluirCampanha(@PathVariable Long id) {
        try {
        	transparenciaRepository.deleteByCampanhaId(id);
        	doacaoRepository.deleteByCampanhaId(id);
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

    private String storeImageAndGetUrl(MultipartFile imagemFile) throws Exception {
        String filename = StringUtils.cleanPath(imagemFile.getOriginalFilename());
        Path uploadDir = Paths.get("uploads");
        Files.createDirectories(uploadDir);
        Path target = uploadDir.resolve(System.currentTimeMillis() + "_" + filename);
        try (InputStream in = imagemFile.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        String fileUrl;
        if (appBaseUrl != null && !appBaseUrl.isBlank()) {
            fileUrl = UriComponentsBuilder.fromUriString(appBaseUrl)
                    .path("/uploads/")
                    .path(target.getFileName().toString())
                    .toUriString();
        } else {
            fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(target.getFileName().toString())
                    .toUriString();
        }

        return fileUrl;
    }
}
