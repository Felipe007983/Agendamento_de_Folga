package Gerenciador.Folga.repository;

import Gerenciador.Folga.model.AgendamentoFolga;
import Gerenciador.Folga.model.Funcionarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoFolgaRepository extends JpaRepository<AgendamentoFolga, Long> {

    /**
     * Verifica se já existe um agendamento de folga para o operador na data especificada.
     *
     * @param operador o operador cujos agendamentos devem ser verificados
     * @param dataFolga a data da folga que deve ser verificada
     * @return true se existir um agendamento; caso contrário, false
     */
    boolean existsByOperadorAndDataFolga(Funcionarios operador, LocalDate dataFolga);

    /**
     * Busca todos os agendamentos de folga para um determinado operador.
     *
     * @param operador o operador cujos agendamentos devem ser buscados
     * @return uma lista de agendamentos de folga para o operador
     */
    List<AgendamentoFolga> findByOperador(Funcionarios operador);

    /**
     * Busca todos os agendamentos de folga em uma data específica.
     *
     * @param dataFolga a data para a qual os agendamentos devem ser buscados
     * @return uma lista de agendamentos de folga na data especificada
     */
    List<AgendamentoFolga> findByDataFolga(LocalDate dataFolga);

    /**
     * Busca todos os agendamentos de folga de um determinado operador em uma data específica.
     *
     * @param operador o operador cujos agendamentos devem ser buscados
     * @param dataFolga a data para a qual os agendamentos devem ser buscados
     * @return uma lista de agendamentos de folga do operador na data especificada
     */
    List<AgendamentoFolga> findByOperadorAndDataFolga(Funcionarios operador, LocalDate dataFolga);
}
