package TGIDTeste.TesteTGID01.Repository;

import TGIDTeste.TesteTGID01.Model.Cliente.cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<cliente, String> {
    Optional<cliente> findByCpf(String cpf);
}
