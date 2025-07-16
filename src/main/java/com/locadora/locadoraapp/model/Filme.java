package com.locadora.locadoraapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "filmes")
@Data
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmeId;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(length = 50)
    private String genero;

    @Column(name = "ano_lancamento") 
    private Integer anoLancamento; 

    @Column(length = 100)
    private String diretor;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;
}