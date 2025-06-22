package br.com.ras.desafio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ras.desafio.dto.ClienteDTO;
import br.com.ras.desafio.model.Cliente;

public class ClienteMapper {
     public static ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setCpf(cliente.getCpf());
        dto.setTelefone(cliente.getTelefone());
        dto.setEmail(cliente.getEmail());
        return dto;
    }

    public static Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEmail(dto.getEmail());
        return cliente;
    }

    public static List<ClienteDTO> toDTOList(List<Cliente> clientes) {
        return clientes.stream()
                       .map(ClienteMapper::toDTO)
                       .collect(Collectors.toList());
    }
}
