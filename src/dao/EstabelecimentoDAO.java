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
}
