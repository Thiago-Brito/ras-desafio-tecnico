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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
public class ContaController {
      

    @PostMapping("/clientes/{idCliente}/contas")
    @Operation(summary = "Criar nova conta para um cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conta criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou valor menor que 0"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "422", description = "Conta com situação CANCELADA não pode ser criada")
    })
    public ResponseEntity<ContaDTO> criarConta(@PathVariable Long idCliente, @RequestBody @Valid ContaDTO dto) {
        return ResponseEntity.ok(contaService.criarConta(idCliente, dto));
    }


    @GetMapping("/{idCliente}/contas")
    @Operation(summary = "Listar contas de um cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Contas listadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<ContaDTO>> listarContas(@PathVariable Long idCliente) {
        return ResponseEntity.ok(contaService.listarContasDoCliente(idCliente));
    }

    
    @PutMapping("/contas/{id}")
    @Operation(summary = "Atualizar uma conta existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<ContaDTO> atualizarConta(@PathVariable Long id, @RequestBody @Valid ContaDTO dto) {
        return ResponseEntity.ok(contaService.atualizarConta(id, dto));
    }

    @DeleteMapping("/contas/{id}")
    @Operation(summary = "Cancelar uma conta (exclusão lógica)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Conta cancelada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<Void> excluirConta(@PathVariable Long id) {
        contaService.excluirConta(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private ContaService contaService;
}
