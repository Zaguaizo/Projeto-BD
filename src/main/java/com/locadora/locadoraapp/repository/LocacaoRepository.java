package com.locadora.locadoraapp.repository;

import com.locadora.locadoraapp.model.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
    List<Locacao> findByCliente_ClienteId(Long clienteId); 
    List<Locacao> findByFilme_FilmeId(Long filmeId);       
    List<Locacao> findByDataDevolucaoIsNull();             
    List<Locacao> findByDataLocacaoBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT l FROM Locacao l JOIN FETCH l.cliente JOIN FETCH l.filme")
    List<Locacao> findAllWithClienteAndFilme();
}