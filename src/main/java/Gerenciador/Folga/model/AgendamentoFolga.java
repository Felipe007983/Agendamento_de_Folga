package Gerenciador.Folga.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "agendamentos_folga")
public class AgendamentoFolga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;

    @ManyToOne
    @JoinColumn(name = "operador_id", nullable = false)
    private Funcionarios operador;

    @Column(name = "data_folga", nullable = false)
    private LocalDate dataFolga;

    // Getters e Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Funcionarios getOperador() {
        return operador;
    }

    public void setOperador(Funcionarios operador) {
        this.operador = operador;
    }

    public LocalDate getDataFolga() {
        return dataFolga;
    }

    public void setDataFolga(LocalDate dataFolga) {
        this.dataFolga = dataFolga;
    }
}
