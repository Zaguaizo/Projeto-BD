package com.locadora.locadoraapp.repository;

import com.locadora.locadoraapp.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    List<Filme> findByGenero(String genero); 
    List<Filme> findByQuantidadeEstoqueGreaterThan(Integer quantidade); 
}