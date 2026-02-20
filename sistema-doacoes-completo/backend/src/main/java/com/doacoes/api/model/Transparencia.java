package com.doacoes.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
