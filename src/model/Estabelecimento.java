package model;
import java.util.UUID;

public class Estabelecimento {
    private String nome;
    private Integer idMaquina;

    public Estabelecimento(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        this.nome = nome;
        this.idMaquina = null; // id ainda não gerado
    }

    public Estabelecimento(String nome, Integer idMaquina) {
        this.nome = nome;
        this.idMaquina = idMaquina;
    }

    public void setIdMaquina(Integer idMaquina) {
        this.idMaquina = idMaquina;
    }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getIdMaquina() { return idMaquina; }

    @Override
    public String toString() {
        return "Estabelecimento{" +
                "nome:'" + nome + '\'';
    }
}