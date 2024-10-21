package Gerenciador.Folga.repository;

import Gerenciador.Folga.model.AgendamentoFolga;
import Gerenciador.Folga.model.Funcionarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AgendamentoFolgaRepository extends JpaRepository<AgendamentoFolga, Long> {
    boolean existsByOperadorAndDataFolga(Funcionarios operador, LocalDate dataFolga);
}
