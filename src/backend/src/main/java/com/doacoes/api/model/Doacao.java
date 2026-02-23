package com.doacoes.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doacoes")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campanha_id")
    private Campanha campanha;

    @ManyToOne
    @JoinColumn(name = "doador_id")
    private Usuario doador;

    private boolean anonimo;

    @NotNull
    private BigDecimal valor;

    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    private StatusDoacao status;

    private String mensagemApoio;

    @PrePersist
    public void prePersist() {
        dataHora = LocalDateTime.now();
        status = StatusDoacao.PENDENTE;
    }

    public enum FormaPagamento {
        PIX,
        CARTAO_CREDITO,
        BOLETO,
        TRANSFERENCIA
    }

    public enum StatusDoacao {
        PENDENTE,
        CONFIRMADA,
        CANCELADA
    }
}
