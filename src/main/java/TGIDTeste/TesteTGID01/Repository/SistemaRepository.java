package TGIDTeste.TesteTGID01.Repository;

import TGIDTeste.TesteTGID01.Model.Sistema.sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SistemaRepository extends JpaRepository<sistema, String> {

    //Seleciona o campo lucro na coluna
    @Query("SELECT s.lucro FROM sistema s WHERE s.id = '1'")
    Optional<Double> findLucro();

    //Atualiza o campo lucro
    @Modifying
    @Transactional
    @Query("UPDATE sistema s SET s.lucro = :lucro WHERE s.id = '1'")
    void updateLucro(Double lucro);

    //Conta os registros
    @Query("SELECT COUNT(s) FROM sistema s WHERE s.id = '1'")
    long countById();
}