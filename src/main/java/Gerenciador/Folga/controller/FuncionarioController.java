package Gerenciador.Folga.controller;

import Gerenciador.Folga.model.AgendamentoFolga;
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
import java.util.Map;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    // Método para exibir o Portal BI
    @GetMapping("/portal-bi")
    public String showPortalBI() {
        return "Portal"; // Nome do arquivo HTML para a página do Portal BI
    }

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

    @GetMapping("/folgas-agendadas")
    public String showFolgasAgendadasPage(Model model) {
        // Adicione dados ao modelo se necessário
        return "FolgasAgendadas"; // Nome do arquivo HTML
    }

    @GetMapping("/folgas-agendadas/data")
    public ResponseEntity<Map<String, Map<LocalDate, List<AgendamentoFolga>>>> getFolgasAgendadas() {
        Map<String, Map<LocalDate, List<AgendamentoFolga>>> folgasAgendadas = funcionarioService.getFolgasAgendadas();
        return ResponseEntity.ok(folgasAgendadas);
    }

    // Método para editar uma folga
    @GetMapping("/folgas-agendadas/editar/{id}")
    public ResponseEntity<AgendamentoFolga> editFolga(@PathVariable Integer id) {
        AgendamentoFolga folga = funcionarioService.getFolgaById(id);
        if (folga != null) {
            return ResponseEntity.ok(folga);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Método alterado para usar PUT com o ID na URL
    @PutMapping("/folgas-agendadas/{id}")
    public ResponseEntity<String> updateFolga(@PathVariable Integer id, @RequestBody AgendamentoFolga updatedFolga) {
        try {
            // Valida se o objeto updatedFolga não é nulo
            if (updatedFolga == null) {
                return ResponseEntity.badRequest().body("O objeto AgendamentoFolga não pode ser nulo.");
            }

            // Valida se o ID do objeto corresponde ao ID na URL
            if (updatedFolga.getId() == null) {
                return ResponseEntity.badRequest().body("O ID da folga não pode ser nulo.");
            }

            // Verifica se o ID da folga corresponde ao ID na URL para evitar inconsistências
            if (!updatedFolga.getId().equals(id)) {
                return ResponseEntity.badRequest().body("O ID da folga não corresponde ao ID fornecido na URL.");
            }

            // Chama o serviço para atualizar a folga
            funcionarioService.updateFolga(updatedFolga);
            return ResponseEntity.ok("Folga atualizada com sucesso.");
        } catch (Exception e) {
            e.printStackTrace(); // Log do erro detalhado
            return ResponseEntity.badRequest().body("Erro ao atualizar a folga: " + e.getMessage());
        }
    }


    // Método para excluir uma folga
    @DeleteMapping("/folgas-agendadas/{id}")
    public ResponseEntity<String> deleteFolga(@PathVariable Integer id) {
        try {
            funcionarioService.deleteFolga(id);
            return ResponseEntity.ok("Folga excluída com sucesso.");
        } catch (Exception e) {
            e.printStackTrace(); // Log do erro detalhado
            return ResponseEntity.badRequest().body("Erro ao excluir a folga: " + e.getMessage());
        }
    }
}
