package com.locadora.locadoraapp.service;

import com.locadora.locadoraapp.model.Filme;
import com.locadora.locadoraapp.repository.FilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmeService {

    @Autowired
    private FilmeRepository filmeRepository;

    public Filme salvarFilme(Filme filme) {
        return filmeRepository.save(filme);
    }

    public Optional<Filme> buscarFilmePorId(Long id) {
        return filmeRepository.findById(id);
    }

    public List<Filme> buscarTodosFilmes() {
        return filmeRepository.findAll();
    }

    public List<Filme> buscarFilmesPorGenero(String genero) {
        return filmeRepository.findByGenero(genero);
    }

    public void deletarFilme(Long id) {
        filmeRepository.deleteById(id);
    }

    public boolean existeFilme(Long id) {
        return filmeRepository.existsById(id);
    }
}