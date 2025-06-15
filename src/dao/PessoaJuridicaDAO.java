package dao;

import model.PessoaJuridica;

import java.sql.*;

public class PessoaJuridicaDAO {
    private Connection conexao;

    public PessoaJuridicaDAO(Connection conexao) {
        this.conexao = conexao;
    }

    //Inserir na tabela usuario
    public int inserir(PessoaJuridica pj) throws SQLException {
        String sql = "INSERT INTO usuario (nome,numero_celular,ativo) VALUES (?,?,?)";
        int idGerado;
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pj.getNome());
            stmt.setString(2, pj.getNumeroCelular());
            stmt.setBoolean(3, pj.isAtivo());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    pj.setId(idGerado); // Define o ID gerado no objeto PessoaJuridica
                } else {
                    throw new SQLException("Erro ao obter o ID gerado.");
                }
            }
        }
        // Inserir na tabela pessoa_juridica
        String sqlPJ = "INSERT INTO pessoa_juridica (id_usuario, cnpj, razao_social) VALUES (?, ?, ?)";
        try (PreparedStatement stmtPJ = conexao.prepareStatement(sqlPJ)) {
            stmtPJ.setInt(1, pj.getId());
            stmtPJ.setString(2, pj.getCnpj());
            stmtPJ.setString(3, pj.getRazaoSocial());
            stmtPJ.executeUpdate();
        }
        return idGerado;
    }
    public PessoaJuridica buscarPorCnpj(String cnpj) throws SQLException {
        String sql = "SELECT * FROM pessoa_juridica WHERE cnpj = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PessoaJuridica(
                        rs.getString("nome"),
                        rs.getString("celular"),
                        rs.getString("cnpj"),
                        rs.getString("razao_social")
                );
            }
            return null;
        }
    }
}
