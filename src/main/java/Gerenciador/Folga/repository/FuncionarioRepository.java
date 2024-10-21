package Gerenciador.Folga.repository;

import Gerenciador.Folga.model.Funcionarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionarios, Long> {

    List<Funcionarios> findByCentroCusto(String centroCusto);

    @Query("SELECT DISTINCT f.centroCusto FROM Funcionarios f")
    List<String> findDistinctCentrosDeCusto();

    // Adicione outros métodos conforme necessário
}
