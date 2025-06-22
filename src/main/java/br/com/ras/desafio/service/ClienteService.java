package br.com.ras.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.ras.desafio.dto.ClienteDTO;
import br.com.ras.desafio.mapper.ClienteMapper;
import br.com.ras.desafio.model.Cliente;
import br.com.ras.desafio.repository.ClienteRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ClienteService {
    
    public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
        logger.info("Iniciando cadastro do cliente: {}", clienteDTO.getNome());
        
        if(clienteDTO.getNome() == null || clienteDTO.getNome().isEmpty()) {
            logger.warn("Nome vazio ao tentar cadastrar cliente");
            throw new IllegalArgumentException("O nome do cliente não pode ser vazio");
        }
        if(clienteDTO.getCpf() == null || clienteDTO.getCpf().isEmpty()) {
            logger.warn("CPF vazio ao tentar cadastrar cliente");
            throw new IllegalArgumentException("O CPF do cliente não pode ser vazio");
        }
        if (clienteRepository.existsByCpf(clienteDTO.getCpf())) {
            logger.warn("CPF já cadastrado: {}", clienteDTO.getCpf());
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF");
        }


        Cliente cliente = ClienteMapper.toEntity(clienteDTO);
        
        Cliente salvo = clienteRepository.save(cliente);
        logger.info("Cliente cadastrado com sucesso: ID={}", salvo.getId());

        return ClienteMapper.toDTO(salvo);
    }

    public List<ClienteDTO> listarTodos() {
        logger.info("Listando todos os clientes");
        List<Cliente> clientes = clienteRepository.findAll();
        return ClienteMapper.toDTOList(clientes);
    }

    public void excluirCliente(Long id) {
        logger.info("Tentando excluir cliente com ID {}", id);
        if (!clienteRepository.existsById(id)) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente com ID " + id + " não encontrado");
        }
        logger.info("Cliente com ID {} excluído com sucesso", id);
        clienteRepository.deleteById(id);
    }

    public Cliente atualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente com ID " + id + " não encontrado"));

        logger.info("Atualizando cliente ID={} com dados: {}", id, clienteDTO);

        if (clienteDTO.getNome() != null && !clienteDTO.getNome().isBlank()) {
            cliente.setNome(clienteDTO.getNome());
        }

        if (clienteDTO.getCpf() != null && !clienteDTO.getCpf().isBlank()) {
            if (!cliente.getCpf().equals(clienteDTO.getCpf()) &&
                clienteRepository.existsByCpf(clienteDTO.getCpf())) {
                logger.warn("Tentativa de atualizar CPF para um já existente: {}", clienteDTO.getCpf());
                throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF");
            }
            cliente.setCpf(clienteDTO.getCpf());
        }

        if (clienteDTO.getTelefone() != null) {
            cliente.setTelefone(clienteDTO.getTelefone());
        }

        if (clienteDTO.getEmail() != null) {
            cliente.setEmail(clienteDTO.getEmail());
        }

        logger.info("Cliente ID={} atualizado com sucesso", id);
        return clienteRepository.save(cliente);
    }



    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

}
