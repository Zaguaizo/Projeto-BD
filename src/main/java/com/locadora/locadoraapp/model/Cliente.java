package com.locadora.locadoraapp.model;

import jakarta.persistence.*; 
import lombok.Data; 

@Entity 
@Table(name = "clientes") 
@Data 
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long clienteId;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String sobrenome;

    @Column(unique = true, nullable = false, length = 255) 
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(length = 255)
    private String endereco;

}