package br.com.ras.desafio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.ras.desafio.dto.ContaDTO;
import br.com.ras.desafio.service.ContaService;
import jakarta.validation.Valid;

@RestController
public class ContaController {
      

    @PostMapping("/clientes/{idCliente}/contas")
    public ResponseEntity<ContaDTO> criarConta(@PathVariable Long idCliente, @RequestBody @Valid ContaDTO dto) {
        return ResponseEntity.ok(contaService.criarConta(idCliente, dto));
    }

    @GetMapping("/{idCliente}/contas")
    public ResponseEntity<List<ContaDTO>> listarContas(@PathVariable Long idCliente) {
        return ResponseEntity.ok(contaService.listarContasDoCliente(idCliente));
    }

    @PutMapping("/contas/{id}")
    public ResponseEntity<ContaDTO> atualizarConta(@PathVariable Long id, @RequestBody @Valid ContaDTO dto) {
        return ResponseEntity.ok(contaService.atualizarConta(id, dto));
    }

    @DeleteMapping("/contas/{id}")
    public ResponseEntity<Void> excluirConta(@PathVariable Long id) {
        contaService.excluirConta(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private ContaService contaService;
}
