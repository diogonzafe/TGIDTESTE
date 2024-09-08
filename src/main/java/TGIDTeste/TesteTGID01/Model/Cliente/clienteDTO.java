package TGIDTeste.TesteTGID01.Model.Cliente;

import jakarta.validation.constraints.Pattern;

public record clienteDTO(
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")
        String cpf,

        String name
) {
}