package br.com.ras.desafio.dto;

import java.math.BigDecimal;

import br.com.ras.desafio.enums.SituacaoConta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaDTO {
    private Long id;

    @Pattern(regexp = "^(0[1-9]|1[0-2])-(\\d{4})$", message = "Formato da referência inválido. Use MM-AAAA")
    private String referencia;

    @DecimalMin(value = "0.00", inclusive = true, message = "O valor não pode ser negativo")
    private BigDecimal valor;

    private SituacaoConta situacao;
}
