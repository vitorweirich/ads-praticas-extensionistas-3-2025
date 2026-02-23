package com.doacoes.api.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.doacoes.api.exceptions.MessageFeedbackException;
import com.doacoes.api.model.Campanha;
import com.doacoes.api.payload.response.MessageResponse;
import com.doacoes.api.repository.CampanhaRepository;
import com.doacoes.api.repository.DoacaoRepository;
import com.doacoes.api.repository.TransparenciaRepository;
import com.doacoes.api.repository.UsuarioRepository;
import com.doacoes.api.security.services.UserDetailsImpl;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    public ResponseEntity<Campanha> obterCampanha(@PathVariable Long id) {
        Optional<Campanha> campanha = campanhaRepository.findById(id);
        if (campanha.isPresent()) {
            return ResponseEntity.ok(campanha.get());
        } else {
        	throw new MessageFeedbackException("Erro: Campanha não encontrada!", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    // TODO: Criar exception handler para o @Valid (geral)
    public ResponseEntity<Campanha> criarCampanha(@Valid @RequestBody Campanha campanha) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl admUser = (UserDetailsImpl) authentication.getPrincipal();
            campanha.setAdministrador(usuarioRepository.getReferenceById(admUser.getId()));          
            Campanha novaCampanha = campanhaRepository.save(campanha);
            return ResponseEntity.ok(novaCampanha);
        } catch (Exception e) {
        	throw new MessageFeedbackException("Erro ao criar campanha: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Campanha> criarCampanhaMultipart(@Valid @RequestPart("campanha") Campanha campanha,
            @RequestPart(value = "imagem", required = false) MultipartFile imagemFile) {
        try {
            if (imagemFile != null && !imagemFile.isEmpty()) {
                String fileUrl = storeImageAndGetUrl(imagemFile, campanha);
                campanha.setImagemCapa(fileUrl);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl admUser = (UserDetailsImpl) authentication.getPrincipal();
            campanha.setAdministrador(usuarioRepository.getReferenceById(admUser.getId()));

            Campanha novaCampanha = campanhaRepository.save(campanha);
            return ResponseEntity.ok(novaCampanha);
        } catch (Exception e) {
        	throw new MessageFeedbackException("Erro ao criar campanha (multipart): " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Campanha> atualizarCampanha(@PathVariable Long id, @Valid @RequestBody Campanha campanhaAtualizada) {
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
        	throw new MessageFeedbackException("Erro: Campanha não encontrada!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Campanha> atualizarCampanhaMultipart(@PathVariable Long id,
            @RequestPart("campanha") Campanha campanhaAtualizada,
            @RequestPart(value = "imagem", required = false) MultipartFile imagemFile) {
        try {
            Optional<Campanha> campanhaData = campanhaRepository.findById(id);
            if (!campanhaData.isPresent()) {
            	throw new MessageFeedbackException("Erro: Campanha não encontrada!", HttpStatus.NOT_FOUND);
            }

            Campanha campanha = campanhaData.get();

            campanha.setTitulo(campanhaAtualizada.getTitulo());
            campanha.setDescricao(campanhaAtualizada.getDescricao());

            if (imagemFile != null && !imagemFile.isEmpty()) {
                String fileUrl = storeImageAndGetUrl(imagemFile, campanha);
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
        	throw new MessageFeedbackException("Erro ao atualizar campanha (multipart): " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Transactional
    public ResponseEntity<MessageResponse> excluirCampanha(@PathVariable Long id) {
        try {
            try {
                Path uploadDir = Paths.get("uploads");
                if (Files.exists(uploadDir) && Files.isDirectory(uploadDir)) {
                    Pattern ptn = Pattern.compile(".*_" + id + "(\\..+)?$");
                    try (Stream<Path> stream = Files.list(uploadDir)) {
                        stream.filter(Files::isRegularFile)
                              .filter(p -> ptn.matcher(p.getFileName().toString()).matches())
                              .forEach(p -> {
                                  try {
                                      Files.deleteIfExists(p);
                                  } catch (Exception ex) {
                                      System.err.println("Erro ao deletar arquivo de upload: " + p + ", " + ex.getMessage());
                                  }
                              });
                    }
                }
            } catch (Exception ex) {
                System.err.println("Erro ao limpar arquivos de upload da campanha " + id + ": " + ex.getMessage());
            }

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

    private String storeImageAndGetUrl(MultipartFile imagemFile, Campanha campanha) throws Exception {
        String originalName = imagemFile.getOriginalFilename();
        String extension = "";
        if (originalName != null) {
            int idx = originalName.lastIndexOf('.');
            if (idx >= 0) {
                extension = originalName.substring(idx);
            }
        }
        String idPart = (campanha != null && campanha.getId() != null) ? campanha.getId().toString() : "tmp";
        String filename = StringUtils.cleanPath(idPart) + extension;
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
