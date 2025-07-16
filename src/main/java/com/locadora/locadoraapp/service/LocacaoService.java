package com.locadora.locadoraapp.service;

import com.locadora.locadoraapp.model.Cliente;
import com.locadora.locadoraapp.model.Filme;
import com.locadora.locadoraapp.model.Locacao;
import com.locadora.locadoraapp.repository.LocacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private ClienteService clienteService; 
    @Autowired
    private FilmeService filmeService;   

    public Locacao salvarLocacao(Locacao locacao) {
        
        if (locacao.getFilme() != null && locacao.getFilme().getQuantidadeEstoque() > 0) {
            locacao.getFilme().setQuantidadeEstoque(locacao.getFilme().getQuantidadeEstoque() - 1);
            filmeService.salvarFilme(locacao.getFilme()); 
        } else if (locacao.getFilme() != null && locacao.getFilme().getQuantidadeEstoque() <= 0) {
            throw new RuntimeException("Filme sem estoque disponível!");
        }
        return locacaoRepository.save(locacao);
    }

    public Optional<Locacao> buscarLocacaoPorId(Long id) {
        return locacaoRepository.findById(id);
    }

    public List<Locacao> buscarTodasLocacoes() {
        return locacaoRepository.findAllWithClienteAndFilme();
    }

    public List<Locacao> buscarLocacoesNaoDevolvidas() {
        return locacaoRepository.findByDataDevolucaoIsNull();
    }

    public List<Locacao> findByFilme_FilmeId(Long filmeId) {
        return locacaoRepository.findByFilme_FilmeId(filmeId);
    }
    
    public Locacao registrarDevolucao(Long locacaoId) {
        Optional<Locacao> optionalLocacao = locacaoRepository.findById(locacaoId);
        if (optionalLocacao.isPresent()) {
            Locacao locacao = optionalLocacao.get();
            if (locacao.getDataDevolucao() == null) {
                locacao.setDataDevolucao(LocalDate.now());
                locacao.getFilme().setQuantidadeEstoque(locacao.getFilme().getQuantidadeEstoque() + 1);
                filmeService.salvarFilme(locacao.getFilme()); 
                return locacaoRepository.save(locacao);
            } else {
                throw new RuntimeException("Locação já devolvida.");
            }
        } else {
            throw new RuntimeException("Locação não encontrada com ID: " + locacaoId);
        }
    }

    public void deletarLocacao(Long id) {
        locacaoRepository.deleteById(id);
    }

    public boolean existeLocacao(Long id) {
        return locacaoRepository.existsById(id);
    }
}