package TGIDTeste.TesteTGID01.Repository;

import TGIDTeste.TesteTGID01.Model.Empresa.empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<empresa, String> {
    Optional<empresa> findByCnpj(String cnpj);
}
