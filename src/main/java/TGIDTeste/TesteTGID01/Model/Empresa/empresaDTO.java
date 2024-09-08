package TGIDTeste.TesteTGID01.Model.Empresa;

import jakarta.validation.constraints.Pattern;

public record empresaDTO(
        @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}")
        String cnpj,
        String name,
        Double saldo
) {
}
