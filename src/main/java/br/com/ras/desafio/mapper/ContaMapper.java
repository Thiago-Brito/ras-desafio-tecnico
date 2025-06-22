package br.com.ras.desafio.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.com.ras.desafio.dto.ContaDTO;
import br.com.ras.desafio.model.Conta;

public class ContaMapper {
    public static ContaDTO toDTO(Conta conta) {
        ContaDTO dto = new ContaDTO();
        dto.setId(conta.getId());
        dto.setValor(conta.getValor());
        dto.setSituacao(conta.getSituacao());
        dto.setReferencia(conta.getReferencia());
        return dto;
    }

    public static List<ContaDTO> toDTOList(List<Conta> contas) {
        return contas.stream()
            .map(ContaMapper::toDTO)
            .collect(Collectors.toList());
    }
}
