package com.locadora.locadoraapp.service;

import com.locadora.locadoraapp.model.Cliente;
import com.locadora.locadoraapp.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id); 
    }

    public List<Cliente> buscarTodosClientes() {
        return clienteRepository.findAll(); 
    }

    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id); 
    }

    public boolean existeCliente(Long id) {
        return clienteRepository.existsById(id);
    }
}