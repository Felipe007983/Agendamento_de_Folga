package Gerenciador.Folga.controller;

import Gerenciador.Folga.model.Funcionarios;
import Gerenciador.Folga.service.FuncionarioService;
import Gerenciador.Folga.utils.DateParser; // Importa a classe DateParser
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public String funcionarios(Model model) {
        // Carregar todos os centros de custo e adicionar ao modelo
        List<String> centrosDeCusto = funcionarioService.getAllCentrosDeCusto();
        model.addAttribute("centrosDeCusto", centrosDeCusto);
        return "funcionarios";
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Funcionarios>> getAllFuncionarios() {
        List<Funcionarios> funcionarios = funcionarioService.getAllFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/centro-custo/{centroCusto}")
    public ResponseEntity<List<Funcionarios>> getFuncionariosByCentroCusto(@PathVariable String centroCusto) {
        List<Funcionarios> funcionarios = funcionarioService.getFuncionariosByCentroCusto(centroCusto);
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/operadores/{centroCusto}")
    public ResponseEntity<List<Funcionarios>> getOperadoresByCentroCusto(@PathVariable String centroCusto) {
        List<Funcionarios> operadores = funcionarioService.getFuncionariosByCentroCusto(centroCusto);
        return ResponseEntity.ok(operadores);
    }

    @GetMapping("/datas-disponiveis/{operadorId}")
    public ResponseEntity<List<LocalDate>> getAvailableDatesForOperator(@PathVariable Long operadorId) {
        List<LocalDate> availableDates = funcionarioService.getAvailableDatesForOperator(operadorId);
        return ResponseEntity.ok(availableDates);
    }

    @PostMapping("/agendar-folga")
    public ResponseEntity<String> scheduleFolga(@RequestParam Long operadorId,
                                                @RequestParam("date") String dateString) {
        LocalDate date;

        // Tenta analisar a string de data
        try {
            date = DateParser.parseDate(dateString); // Usa o DateParser
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Retorna mensagem de erro específica
        }

        System.out.println("Data recebida: " + date); // Log para depuração

        try {
            // Agenda a folga e verifica se já existe um agendamento
            funcionarioService.scheduleFolga(operadorId, date);
            return ResponseEntity.ok("Folga agendada com sucesso.");
        } catch (IllegalArgumentException e) {
            // Captura a exceção específica para agendamentos duplicados
            return ResponseEntity.badRequest().body(e.getMessage()); // Retorna mensagem de erro específica
        } catch (Exception e) {
            // Log do erro detalhado para entender o que deu errado
            e.printStackTrace(); // Adicionando stack trace para depuração
            return ResponseEntity.badRequest().body("Erro ao processar a data: " + e.getMessage());
        }
    }

}
