package br.com.ras.desafio.dto;

import java.math.BigDecimal;

import br.com.ras.desafio.enums.SituacaoConta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaDTO {
    
    @Schema(hidden = true)
    private Long id;

    @Pattern(regexp = "^(0[1-9]|1[0-2])-(\\d{4})$", message = "Formato da referência inválido. Use MM-AAAA")
    @Schema(example = "06-2025", description = "Referência da conta no formato MM-AAAA")
    private String referencia;

    @DecimalMin(value = "0.00", inclusive = true, message = "O valor não pode ser negativo")
    @Schema(example = "150.00", description = "Valor da conta. Deve ser zero ou maior")
    private BigDecimal valor;

    @Schema(example = "PENDENTE", description = "Situação da conta: PENDENTE, PAGA ou CANCELADA")
    private SituacaoConta situacao;
}
