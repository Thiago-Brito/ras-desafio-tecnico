package br.com.ras.desafio.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ras.desafio.model.Conta;


public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByClienteId(Long clienteId);
}