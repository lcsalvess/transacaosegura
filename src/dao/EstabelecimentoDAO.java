package dao;

import model.Estabelecimento;

import java.sql.*;

public class EstabelecimentoDAO {
    private Connection conexao;

    public EstabelecimentoDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
    }


    // Inserir estabelecimento e obter o ID gerado
    public int inserir(Estabelecimento estabelecimento) throws SQLException {
        String sql = "INSERT INTO estabelecimento (nome) VALUES (?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, estabelecimento.getNome());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir estabelecimento, nenhuma linha afetada.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    estabelecimento.setIdMaquina(idGerado);
                    return idGerado;
                } else {
                    throw new SQLException("Falha ao obter o ID gerado para estabelecimento.");
                }
            }
        }
    }

    public Estabelecimento buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome FROM estabelecimento WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Estabelecimento est = new Estabelecimento();
                    est.setIdMaquina(rs.getInt("id"));
                    est.setNome(rs.getString("nome"));
                    return est;
                }
            }
        }
        return null;  // Não encontrado
    }

    public Estabelecimento buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT id, nome FROM estabelecimento WHERE nome = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Estabelecimento est = new Estabelecimento();
                    est.setIdMaquina(rs.getInt("id"));
                    est.setNome(rs.getString("nome"));
                    return est;
                }
                return null; // não encontrou estabelecimento com esse nome
            }
        }
    }
}
