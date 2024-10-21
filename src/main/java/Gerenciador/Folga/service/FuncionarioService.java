package Gerenciador.Folga.service;

import Gerenciador.Folga.model.AgendamentoFolga;
import Gerenciador.Folga.model.Funcionarios;
import Gerenciador.Folga.repository.AgendamentoFolgaRepository;
import Gerenciador.Folga.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AgendamentoFolgaRepository agendamentoFolgaRepository;

    public List<Funcionarios> getAllFuncionarios() {
        return funcionarioRepository.findAll();
    }

    public List<Funcionarios> getFuncionariosByCentroCusto(String centroCusto) {
        return funcionarioRepository.findByCentroCusto(centroCusto);
    }

    public List<Funcionarios> getOperadoresByCentroCusto(String centroCusto) {
        return funcionarioRepository.findByCentroCusto(centroCusto);
    }

    public List<String> getAllCentrosDeCusto() {
        return funcionarioRepository.findDistinctCentrosDeCusto();
    }

    public List<LocalDate> getAvailableDatesForOperator(Long operadorId) {
        int diasFuturos = 30;
        LocalDate hoje = LocalDate.now();

        return Stream.iterate(hoje, date -> date.plusDays(1))
                .limit(diasFuturos)
                .collect(Collectors.toList());
    }

    public void scheduleFolga(Long operadorId, LocalDate date) {
        Funcionarios operador = funcionarioRepository.findById(operadorId)
                .orElseThrow(() -> new RuntimeException("Operador não encontrado"));

        boolean exists = agendamentoFolgaRepository.existsByOperadorAndDataFolga(operador, date);
        if (exists) {
            throw new IllegalArgumentException("Já existe uma folga agendada para este operador nesta data.");
        }

        AgendamentoFolga agendamento = new AgendamentoFolga();
        agendamento.setOperador(operador);
        agendamento.setDataFolga(date);

        agendamentoFolgaRepository.save(agendamento);
    }

    public Map<String, Map<LocalDate, List<AgendamentoFolga>>> getFolgasAgendadas() {
        List<AgendamentoFolga> folgas = agendamentoFolgaRepository.findAll();

        return folgas.stream()
                .collect(Collectors.groupingBy(
                        agendamento -> agendamento.getOperador().getCentroCusto(),
                        Collectors.groupingBy(
                                AgendamentoFolga::getDataFolga
                        )
                ));
    }

    public AgendamentoFolga getFolgaById(Integer id) {
        return agendamentoFolgaRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Folga não encontrada"));
    }

    public void updateFolga(AgendamentoFolga updatedFolga) {
        if (updatedFolga == null) {
            throw new IllegalArgumentException("O objeto updatedFolga não pode ser nulo.");
        }

        Integer id = updatedFolga.getId();
        if (id == null) {
            throw new IllegalArgumentException("ID da folga não pode ser nulo.");
        }

        AgendamentoFolga existingFolga = getFolgaById(id);
        if (existingFolga == null) {
            throw new IllegalArgumentException("Folga não encontrada para o ID fornecido.");
        }

        // Comparação da data da folga
        if (!existingFolga.getDataFolga().equals(updatedFolga.getDataFolga())) {
            boolean exists = agendamentoFolgaRepository.existsByOperadorAndDataFolga(existingFolga.getOperador(), updatedFolga.getDataFolga());
            if (exists) {
                throw new IllegalArgumentException("Já existe uma folga agendada para este operador nesta nova data.");
            }
        }

        // Atualiza a data da folga
        existingFolga.setDataFolga(updatedFolga.getDataFolga());
        // Atualize outros campos de existingFolga conforme necessário
        agendamentoFolgaRepository.save(existingFolga);
    }


    public void deleteFolga(Integer id) { // Alterado para Integer
        AgendamentoFolga folga = getFolgaById(id);
        agendamentoFolgaRepository.delete(folga);
    }
}
