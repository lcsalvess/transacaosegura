package transacaosegura;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Transacao {
    public enum Status { PENDENTE, APROVADA, RECUSADA }

    private final String id;
    private final String idUser;
    private final double valor;
    private Status status;
    private final LocalDateTime dataHora;
    private final Estabelecimento estabelecimento;

    public Transacao(String idUser, double valor, Estabelecimento estabelecimento) {
        this.id = UUID.randomUUID().toString();
        this.idUser = idUser;
        this.valor = valor;
        this.status = Status.PENDENTE; // A transação começa como PENDENTE
        this.dataHora = LocalDateTime.now();
        this.estabelecimento = estabelecimento;
    }

    public String getId() { return id; }
    public String getIdUser() { return idUser; }
    public double getValor() { return valor; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Estabelecimento getEstabelecimento() { return estabelecimento; }

    // --- NOVO MÉTODO PARA VERIFICAR SE A CONFIRMAÇÃO É NECESSÁRIA ---
    public boolean isConfirmacaoNecessaria() {
        return this.valor > 50.00; // A regra de negócio está aqui
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataHoraFormatada = dataHora.format(dtf);
        return "Transação{" +
                "Número:'" + id + '\'' +
                ", valor: R$" + String.format("%.2f", valor) +
                ", status:" + status +
                ", dataHora:" + dataHoraFormatada +
                ", estabelecimento:" + estabelecimento.getNome() +
                '}';
    }
}