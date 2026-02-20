package com.doacoes.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private LocalDate dataTermino;

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

    @PrePersist
    public void prePersist() {
        valorArrecadado = BigDecimal.ZERO;
        status = StatusCampanha.ATIVA;
    }

    public enum StatusCampanha {
        ATIVA,
        FINALIZADA,
        CANCELADA
    }
}
