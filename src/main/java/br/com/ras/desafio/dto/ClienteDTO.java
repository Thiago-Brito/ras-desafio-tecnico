package br.com.ras.desafio.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    @Schema(hidden = true)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do cliente", example = "Thiago Brito")
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
    @Schema(description = "CPF do cliente (apenas números)", example = "12345678900")
    private String cpf;

    @Schema(description = "Telefone com DDD", example = "81999999999")
    private String telefone;
    
    @Email(message = "Email inválido")
    @Schema(description = "Email válido do cliente", example = "thiago@email.com")
    private String email;


}
