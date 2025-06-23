package br.com.ras.desafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.com.ras.desafio.dto.ContaDTO;
import br.com.ras.desafio.enums.SituacaoConta;
import br.com.ras.desafio.model.Cliente;
import br.com.ras.desafio.model.Conta;
import br.com.ras.desafio.repository.ClienteRepository;
import br.com.ras.desafio.repository.ContaRepository;


@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {
    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Test
    void deveCriarContaComSucesso() {
        ContaDTO dto = new ContaDTO(null, "06-2025", new BigDecimal("150.00"), SituacaoConta.PENDENTE);
        Cliente cliente = new Cliente(1L, "Maria", "12345678900", null, null);
        Conta conta = new Conta(1L, "06-2025", new BigDecimal("150.00"), SituacaoConta.PENDENTE, cliente);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(contaRepository.save(any())).thenReturn(conta);

        ContaDTO result = contaService.criarConta(1L, dto);

        assertNotNull(result);
        assertEquals("06-2025", result.getReferencia());
        verify(contaRepository).save(any());
    }

    @Test
    void deveLancarErroAoCriarContaComValorNegativo() {
        ContaDTO dto = new ContaDTO(null, "06-2025", new BigDecimal("-10.00"), SituacaoConta.PENDENTE);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> contaService.criarConta(1L, dto));
        assertEquals("400 BAD_REQUEST \"Valor da conta nao pode ser negativo\"", exception.getMessage());
    }

    @Test
    void deveLancarErroAoCriarContaCancelada() {
        ContaDTO dto = new ContaDTO(null, "06-2025", new BigDecimal("100.00"), SituacaoConta.CANCELADA);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            contaService.criarConta(1L, dto);
        });

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
        assertEquals("Não é possível criar conta já cancelada", ex.getReason());
    }

    @Test
    void deveListarContasDeCliente() {
        Cliente cliente = new Cliente(1L, "Maria", "12345678900", null, null);
        Conta conta = new Conta(1L, "06-2025", new BigDecimal("150.00"), SituacaoConta.PENDENTE, cliente);

        when(contaRepository.findByClienteId(1L)).thenReturn(List.of(conta));

        List<ContaDTO> contas = contaService.listarContasDoCliente(1L);

        assertEquals(1, contas.size());
        assertEquals("06-2025", contas.get(0).getReferencia());
    }

    @Test
    void deveCancelarConta() {
        Cliente cliente = new Cliente(1L, "Maria", "12345678900", null, null);
        Conta conta = new Conta(1L, "06-2025", new BigDecimal("150.00"), SituacaoConta.PENDENTE, cliente);

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        contaService.excluirConta(1L);

        assertEquals(SituacaoConta.CANCELADA, conta.getSituacao());
        verify(contaRepository).save(conta);
    }

    @Test
    void deveLancarErroAoCancelarContaInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> contaService.excluirConta(99L));
        assertEquals("404 NOT_FOUND \"Conta nao encontrada\"", exception.getMessage());
    }

}
