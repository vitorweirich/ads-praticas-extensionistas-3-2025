package com.doacoes.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    // TODO: Validar se faz sentido manter ou remover
    private LocalDate dataInicio;

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
        // TODO: Avaliar se faz sentido setar esses valores dessa forma
        // ou se deveria ser um fallback, ou seja, se o usuário não setar
        // o valor, setar o padrão
        dataInicio = LocalDate.now();
        valorArrecadado = BigDecimal.ZERO;
        status = StatusCampanha.ATIVA;
    }

    public enum StatusCampanha {
        ATIVA,
        FINALIZADA,
        CANCELADA
    }
}
