package com.locadora.locadoraapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate; 

@Entity
@Table(name = "locacoes")
@Data
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locacaoId;

    @ManyToOne 
    @JoinColumn(name = "cliente_id", nullable = false) 
    private Cliente cliente; 

    @ManyToOne 
    @JoinColumn(name = "filme_id", nullable = false) 
    private Filme filme; 

    @Column(name = "data_locacao", nullable = false)
    private LocalDate dataLocacao;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @PrePersist 
    protected void onCreate() {
        if (dataLocacao == null) {
            dataLocacao = LocalDate.now();
        }
    }
}