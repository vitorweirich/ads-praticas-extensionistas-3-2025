package com.doacoes.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transparencia")
public class Transparencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campanha_id")
    private Campanha campanha;

    @NotBlank
    private String tituloAlocacao;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String descricaoAlocacao;

    @NotNull
    private BigDecimal valorAlocado;

    private LocalDateTime dataAlocacao;

    private String comprovante;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    @PrePersist
    public void prePersist() {
        dataAlocacao = LocalDateTime.now();
    }
}
