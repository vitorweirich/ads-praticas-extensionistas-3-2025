package com.doacoes.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private String comprovante;

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
