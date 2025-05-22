package com.doacoes.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 120)
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    private LocalDateTime dataCadastro;

    private String endereco;

    private String telefone;

    private String cpfCnpj;

    private boolean ativo;

    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
        ativo = true;
    }

    public enum TipoUsuario {
        DOADOR,
        ADMINISTRADOR
    }
}
