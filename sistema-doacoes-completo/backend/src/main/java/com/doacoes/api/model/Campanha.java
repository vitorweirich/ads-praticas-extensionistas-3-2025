package com.doacoes.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "campanhas")
public class Campanha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String titulo;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String imagemCapa;

    @NotNull
    private BigDecimal metaFinanceira;

    private BigDecimal valorArrecadado;

    private LocalDateTime dataInicio;

    private LocalDateTime dataTermino;

    @Enumerated(EnumType.STRING)
    private StatusCampanha status;

    @NotBlank
    @Size(max = 50)
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "administrador_id")
    @JsonIgnore
    // TODO: Criar DTOs ao invés de retornar as entidades
    private Usuario administrador;

    @NotBlank
    private String beneficiarios;

    private String galeriaImagens;

    @PrePersist
    public void prePersist() {
        dataInicio = LocalDateTime.now();
        valorArrecadado = BigDecimal.ZERO;
        status = StatusCampanha.ATIVA;
    }

    public enum StatusCampanha {
        ATIVA,
        FINALIZADA,
        CANCELADA
    }
}
