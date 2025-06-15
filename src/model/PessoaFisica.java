package model;

public class PessoaFisica extends Usuario { // PessoaFisica "é um" Usuario
    private String cpf;

    public PessoaFisica(String nome, String numeroCelular, String cpf) {
        super(nome, numeroCelular); // Chama o construtor da classe Usuario
        this.cpf = cpf;
    }

    public PessoaFisica() {
        super();
    }

    public void setId(int id) {
        super.setId(id); // Chama o metodo setId da classe Usuario
    }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    @Override
    public String toString() {
        // Reutiliza o toString da superclasse e adiciona o CPF
        return "PessoaFisica{" +
               "nome='" + getNome() + '\'' + // Reutiliza o nome do Usuario
               ", numeroCelular='" + getNumeroCelular() + '\'' + // Reutiliza o celular do Usuario
               ", cpf='" + cpf + '\'' +
               '}';
    }
}