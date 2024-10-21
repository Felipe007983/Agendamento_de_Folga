package Gerenciador.Folga.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Funcionarios {
    @Id
    private Integer  id;
    private String nome;

    // Usando @Column para especificar o nome exato da coluna no banco de dados
    @Column(name = "centrocusto")
    private String centroCusto;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }
}
