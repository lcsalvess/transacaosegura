package dao;

import model.PessoaFisica;

import java.sql.*;

public class PessoaFisicaDAO {
    private final Connection conexao;

    public PessoaFisicaDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
    }

    //Inserir na tabela usuario
    public int inserir(PessoaFisica pf) throws SQLException {
        String sql = "INSERT INTO usuario (nome,numero_celular,ativo) VALUES (?,?,?)";
        int idGerado;
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pf.getNome());
            stmt.setString(2, pf.getNumeroCelular());
            stmt.setBoolean(3, pf.isAtivo());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    pf.setId(idGerado); // Define o ID gerado no objeto PessoaFisica
                } else {
                    throw new SQLException("Erro ao obter o ID gerado.");
                }
            }
        }
        // Inserir na tabela pessoa_fisica
        String sqlPF = "INSERT INTO pessoa_fisica (id_usuario, cpf) VALUES (?, ?)";
        try (PreparedStatement stmtPF = conexao.prepareStatement(sqlPF)) {
            stmtPF.setInt(1, pf.getId());
            stmtPF.setString(2, pf.getCpf());
            stmtPF.executeUpdate();
        }
        return idGerado;
    }
}
