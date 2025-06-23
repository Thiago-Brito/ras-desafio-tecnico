package br.com.ras.desafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import br.com.ras.desafio.dto.ClienteDTO;
import br.com.ras.desafio.mapper.ClienteMapper;
import br.com.ras.desafio.model.Cliente;
import br.com.ras.desafio.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    
    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Test
    void deveCriarClienteComSucesso() {
        Cliente cliente = new Cliente(1L, "João", "12345678901", "81999999999", "joao@email.com");
        ClienteDTO dto = ClienteMapper.toDTO(cliente);;


        when(clienteRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(clienteRepository.save(any())).thenReturn(cliente);

        ClienteDTO result = clienteService.criarCliente(dto);

        assertNotNull(result.getId());
        assertEquals("João", result.getNome());
        verify(clienteRepository).save(any());
    }

    @Test
    void deveFalharCriacaoClienteComNomeVazio() {
        ClienteDTO dto = new ClienteDTO(null, "", "12345678901", "81999999999", "joao@email.com");
        var ex = assertThrows(ResponseStatusException.class, () -> clienteService.criarCliente(dto));
        assertEquals("O nome do cliente não pode ser vazio", ex.getReason());
    }

    @Test
    void deveFalharCriacaoClienteComCpfRepetido() {
        ClienteDTO dto = new ClienteDTO(null, "João", "12345678901", "81999999999", "joao@email.com");
        when(clienteRepository.existsByCpf(dto.getCpf())).thenReturn(true);
        var ex = assertThrows(ResponseStatusException.class, () -> clienteService.criarCliente(dto));
        assertEquals("Já existe um cliente cadastrado com este CPF", ex.getReason());
    }


    @Test
    void deveAtualizarClienteComSucesso() {
        Cliente cliente = new Cliente(1L, "João", "12345678901", null, null);
        ClienteDTO dto = new ClienteDTO(null, "João Silva", "12345678901", "81999999999", "novo@email.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(cliente);

        ClienteDTO atualizado = clienteService.atualizarCliente(1L, dto);

        assertEquals("João Silva", atualizado.getNome());
        assertEquals("81999999999", atualizado.getTelefone());
    }

    @Test
    void deveLancarErroAoAtualizarClienteNaoExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        var ex = assertThrows(ResponseStatusException.class, () -> clienteService.atualizarCliente(1L, new ClienteDTO()));
        assertEquals("Cliente com ID 1 não encontrado", ex.getReason());
    }

     @Test
    void deveListarTodosClientes() {
        List<Cliente> clientes = Arrays.asList(
            new Cliente(1L, "João", "12345678901", null, null),
            new Cliente(2L, "Maria", "10987654321", null, null)
        );

        when(clienteRepository.findAll()).thenReturn(clientes);

        List<ClienteDTO> result = clienteService.listarTodos();

        assertEquals(2, result.size());
    }

    @Test
    void deveExcluirClienteComSucesso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        clienteService.excluirCliente(1L);
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void deveLancarErroAoExcluirClienteNaoExistente() {
        when(clienteRepository.existsById(1L)).thenReturn(false);
        var ex = assertThrows(ResponseStatusException.class, () -> clienteService.excluirCliente(1L));
        assertEquals("Cliente com ID 1 não encontrado", ex.getReason());
    }

}
