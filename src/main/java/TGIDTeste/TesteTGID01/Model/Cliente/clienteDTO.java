package TGIDTeste.TesteTGID01.Model.Cliente;

import jakarta.validation.constraints.Pattern;

public record clienteDTO(
        //valida formato CPF
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")
        String cpf,
        String email,

        String name
) {
}