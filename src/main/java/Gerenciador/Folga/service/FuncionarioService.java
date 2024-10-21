package Gerenciador.Folga.service;

import Gerenciador.Folga.model.AgendamentoFolga;
import Gerenciador.Folga.model.Funcionarios;
import Gerenciador.Folga.repository.AgendamentoFolgaRepository;
import Gerenciador.Folga.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
        // Buscar todos os operadores associados a um centro de custo específico.
        return funcionarioRepository.findByCentroCusto(centroCusto);
    }

    public List<String> getAllCentrosDeCusto() {
        // Método que retorna todos os centros de custo disponíveis.
        return funcionarioRepository.findDistinctCentrosDeCusto();
    }

    public List<LocalDate> getAvailableDatesForOperator(Long operadorId) {
        // Defina o número de dias futuros para os quais deseja mostrar as datas disponíveis.
        int diasFuturos = 30;
        LocalDate hoje = LocalDate.now();

        // Cria uma lista de datas de hoje até os próximos 30 dias.
        return Stream.iterate(hoje, date -> date.plusDays(1))
                .limit(diasFuturos)
                .collect(Collectors.toList());
    }

    public void scheduleFolga(Long operadorId, LocalDate date) {
        // Buscar o operador pelo ID
        Funcionarios operador = funcionarioRepository.findById(operadorId)
                .orElseThrow(() -> new RuntimeException("Operador não encontrado"));

        // Verificar se já existe um agendamento de folga para o operador na data especificada
        boolean exists = agendamentoFolgaRepository.existsByOperadorAndDataFolga(operador, date);
        if (exists) {
            throw new IllegalArgumentException("Já existe uma folga agendada para este operador nesta data.");
        }

        // Criar um novo agendamento de folga
        AgendamentoFolga agendamento = new AgendamentoFolga();
        agendamento.setOperador(operador);
        agendamento.setDataFolga(date);

        // Salvar o agendamento no banco de dados
        agendamentoFolgaRepository.save(agendamento);
    }
}
