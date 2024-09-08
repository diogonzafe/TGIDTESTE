package TGIDTeste.TesteTGID01.Model.Transacoes;

import TGIDTeste.TesteTGID01.Model.Cliente.cliente;
import TGIDTeste.TesteTGID01.Model.Empresa.empresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "transacoes")
@Entity(name = "transacoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class transacoes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double deposito;

    private Double saque;

    @ManyToOne
    @JoinColumn(name = "cpf", nullable = false)
    private cliente cliente;

    @ManyToOne
    @JoinColumn(name = "cnpj", nullable = false)
    private empresa empresa;


    public transacoes(transacoesDTO transacoesDTO, cliente cliente, empresa empresa){
        this.deposito = transacoesDTO.deposito();
        this.saque = transacoesDTO.saque();
        this.cliente = cliente;
        this.empresa = empresa;
    }
}
