package transacaosegura;
import java.util.UUID;

public class Estabelecimento {
    private String nome;
    private final String idMaquina;

    public Estabelecimento(String nome, String idMaquina) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        this.nome = nome;
        this.idMaquina = (idMaquina == null || idMaquina.isEmpty())
                //if
                ? UUID.randomUUID().toString()
                //else
                : idMaquina;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getIdMaquina() { return idMaquina; }

    @Override
    public String toString() {
        return "Establishment{" +
                "nome='" + nome + '\'' +
                ", idMaquina='" + idMaquina + '\'' +
                '}';
    }
}