package transacaosegura;

public class PessoaJuridica extends Usuario { // PessoaJuridica "é um" Usuario
    private String cnpj;
    private String razaoSocial;

    public PessoaJuridica(String nome, String numeroCelular, String cnpj, String razaoSocial) {
        super(nome, numeroCelular); // Chama o construtor da classe Usuario
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    @Override
    public String toString() {
        // Reutiliza o toString da superclasse e adiciona CNPJ e Razão Social
        return "PessoaJuridica{" +
               "id='" + getId() + '\'' +
               ", nome='" + getNome() + '\'' +
               ", numeroCelular='" + getNumeroCelular() + '\'' +
               ", ativo=" + isAtivo() +
               ", cnpj='" + cnpj + '\'' +
               ", razaoSocial='" + razaoSocial + '\'' +
               '}';
    }
}