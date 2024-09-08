package TGIDTeste.TesteTGID01.Repository;

import TGIDTeste.TesteTGID01.Model.Transacoes.transacoes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacoesRepository extends JpaRepository<transacoes, String> {
}
