package TGIDTeste.TesteTGID01.Model.Cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Table(name = "cliente")
@Entity(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class cliente {
    @Id
    @Column(unique = true)
    private String cpf;

    @NotNull
    private String name;

    public cliente(clienteDTO clienteDTO){

        this.cpf = clienteDTO.cpf();
        this.name = clienteDTO.name();
    }
}
