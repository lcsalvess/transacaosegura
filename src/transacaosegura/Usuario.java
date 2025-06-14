package transacaosegura;

import java.util.UUID;

public class Usuario {
    private final String id;
    private String nome;
    private String numeroCelular;
    private boolean ativo;

    public Usuario(String nome, String numeroCelular) {
        this.id = UUID.randomUUID().toString(); // ID aleatório e único
        this.nome = nome;
        this.numeroCelular = numeroCelular;
        this.ativo = true;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getNumeroCelular() { return numeroCelular; }
    public void setNumeroCelular(String numeroCelular) { this.numeroCelular = numeroCelular; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return "User{" +
                "nome='" + nome + '\'' +
                ", numeroCelular='" + numeroCelular + '\'' +
                '}';
    }
}