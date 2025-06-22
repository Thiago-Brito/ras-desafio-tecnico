package br.com.ras.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ras.desafio.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
}