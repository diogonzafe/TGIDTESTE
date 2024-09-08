package TGIDTeste.TesteTGID01.Model.Empresa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "empresa")
@Entity(name = "empresa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class empresa {
    @Id
    private String cnpj;

    private String name;

    private Double saldo;

    public empresa(empresaDTO empresaDTO){

        this.cnpj = empresaDTO.cnpj();
        this.name = empresaDTO.name();
        this.saldo = empresaDTO.saldo();
    }
}