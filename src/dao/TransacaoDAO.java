package dao;

import model.Transacao;

import java.sql.*;

public class TransacaoDAO {
    private final Connection conexao;

    public TransacaoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // Inserir uma transação (id já é gerado no objeto UUID)
    public void inserir(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO transacao (id, id_usuario, valor, status, data_hora, id_estabelecimento) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, transacao.getId()); // UUID String
            stmt.setInt(2, transacao.getIdUser());
            stmt.setDouble(3, transacao.getValor());
            stmt.setString(4, transacao.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(transacao.getDataHora()));
            stmt.setInt(6, transacao.getEstabelecimento().getIdMaquina()); // Assumindo int id
            stmt.executeUpdate();
        }
    }
}
