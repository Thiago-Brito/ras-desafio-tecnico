package br.com.ras.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.ras.desafio.dto.ContaDTO;
import br.com.ras.desafio.enums.SituacaoConta;
import br.com.ras.desafio.mapper.ContaMapper;
import br.com.ras.desafio.model.Cliente;
import br.com.ras.desafio.model.Conta;
import br.com.ras.desafio.repository.ClienteRepository;
import br.com.ras.desafio.repository.ContaRepository;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ContaService {
    

    public ContaDTO criarConta(Long idCliente, ContaDTO dto) {
        logger.info("Criando conta para cliente ID={} com dados: {}", idCliente, dto);

        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) < 0) {
            logger.warn("Tentativa de criar conta com valor negativo: {}", dto.getValor());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor da conta não pode ser negativo");
        }
        if (dto.getSituacao() == null) {
            logger.warn("Tentativa de criar conta sem situação definida");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Situação da conta não pode ser nula");
        }

        if (dto.getSituacao() == SituacaoConta.CANCELADA) {
            logger.warn("Tentativa de criar conta já cancelada");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Não é possível criar conta já cancelada");
        }
        if (dto.getReferencia() == null || dto.getReferencia().isEmpty()) {
            logger.warn("Tentativa de criar conta sem referência definida");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Referência da conta não pode ser nula ou vazia");
        }

        Cliente cliente = clienteRepository.findById(idCliente)
            .orElseThrow(() -> {
                logger.warn("Cliente com ID={} não encontrado ao tentar criar conta", idCliente);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
            });

        Conta conta = new Conta();
        conta.setValor(dto.getValor());
        conta.setReferencia(dto.getReferencia());
        conta.setSituacao(dto.getSituacao());
        conta.setCliente(cliente);

        Conta salva = contaRepository.save(conta);
        logger.info("Conta criada com sucesso: ID={}", salva.getId());

        return ContaMapper.toDTO(salva);
    }

    public List<ContaDTO> listarContasDoCliente(Long idCliente) {
        logger.info("Listando contas do cliente ID={}", idCliente);
    
        if (!clienteRepository.existsById(idCliente)) {
            logger.warn("Cliente com ID={} não encontrado", idCliente);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }

        List<Conta> contas = contaRepository.findByClienteId(idCliente);
        return ContaMapper.toDTOList(contas);
    }

    public ContaDTO atualizarConta(Long id, ContaDTO dto) {
        logger.info("Atualizando conta ID={} com dados: {}", id, dto);

        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Conta ID={} não encontrada para atualização", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada");
            });

        if (dto.getValor() != null && dto.getValor().compareTo(BigDecimal.ZERO) < 0) {
            logger.warn("Tentativa de atualizar conta ID={} com valor negativo: {}", id, dto.getValor());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor da conta não pode ser negativo");
        }
        if (dto.getValor() != null) conta.setValor(dto.getValor());
        if (dto.getSituacao() != null) conta.setSituacao(dto.getSituacao());
        if (dto.getReferencia() != null && !dto.getReferencia().isEmpty()) {
            conta.setReferencia(dto.getReferencia());
        }

        Conta atualizada = contaRepository.save(conta);
        logger.info("Conta ID={} atualizada com sucesso", id);
        return ContaMapper.toDTO(atualizada);
    }

    public void excluirConta(Long id) {
        logger.info("Cancelando conta ID={}", id);

        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Conta ID={} não encontrada para exclusão lógica", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada");
            });

        conta.setSituacao(SituacaoConta.CANCELADA);
        contaRepository.save(conta);
        logger.info("Conta ID={} cancelada com sucesso", id);
    }

    private static final Logger logger = LoggerFactory.getLogger(ContaService.class);

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;
}
