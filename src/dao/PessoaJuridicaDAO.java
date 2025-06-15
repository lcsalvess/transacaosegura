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
        String sql = "SELECT u.nome, u.numero_celular, pj.cnpj, pj.razao_social " +
                "FROM pessoa_juridica pj " +
                "JOIN usuario u ON pj.id_usuario = u.id " +
                "WHERE pj.cnpj = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PessoaJuridica(
                        rs.getString("nome"),
                        rs.getString("numero_celular"),
                        rs.getString("cnpj"),
                        rs.getString("razao_social")
                );
            }
            return null;
        }
    }

    public void atualizar(PessoaJuridica pj) throws SQLException {
        // Primeiro, recuperar o ID do usuário com base no CNPJ
        String sqlBuscarId = "SELECT id_usuario FROM pessoa_juridica WHERE cnpj = ?";
        int idUsuario;

        try (PreparedStatement stmtBuscar = conexao.prepareStatement(sqlBuscarId)) {
            stmtBuscar.setString(1, pj.getCnpj());
            ResultSet rs = stmtBuscar.executeQuery();
            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario");
            } else {
                throw new SQLException("CNPJ não encontrado.");
            }
        }

        // Agora, atualizar os dados na tabela usuario
        String sqlAtualizarUsuario = "UPDATE usuario SET numero_celular = ? WHERE id = ?";
        try (PreparedStatement stmtAtualizarUsuario = conexao.prepareStatement(sqlAtualizarUsuario)) {
            stmtAtualizarUsuario.setString(1, pj.getNumeroCelular());
            stmtAtualizarUsuario.setInt(2, idUsuario);
            stmtAtualizarUsuario.executeUpdate();
        }

        // E atualizar a razão social na tabela pessoa_juridica
        String sqlAtualizarPJ = "UPDATE pessoa_juridica SET razao_social = ? WHERE cnpj = ?";
        try (PreparedStatement stmtAtualizarPJ = conexao.prepareStatement(sqlAtualizarPJ)) {
            stmtAtualizarPJ.setString(1, pj.getRazaoSocial());
            stmtAtualizarPJ.setString(2, pj.getCnpj());
            stmtAtualizarPJ.executeUpdate();
        }
    }
    public void deletarPorCnpj(String cnpj) throws SQLException {
        // Buscar o ID do usuário pelo CNPJ
        String sqlBuscarId = "SELECT id_usuario FROM pessoa_juridica WHERE cnpj = ?";
        int idUsuario;

        try (PreparedStatement stmtBuscar = conexao.prepareStatement(sqlBuscarId)) {
            stmtBuscar.setString(1, cnpj);
            ResultSet rs = stmtBuscar.executeQuery();
            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario");
            } else {
                throw new SQLException("CNPJ não encontrado.");
            }
        }

        // Deletar da tabela pessoa_juridica
        String sqlDeletarPJ = "DELETE FROM pessoa_juridica WHERE cnpj = ?";
        try (PreparedStatement stmtPJ = conexao.prepareStatement(sqlDeletarPJ)) {
            stmtPJ.setString(1, cnpj);
            stmtPJ.executeUpdate();
        }

        // Deletar da tabela usuario
        String sqlDeletarUsuario = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement stmtUsuario = conexao.prepareStatement(sqlDeletarUsuario)) {
            stmtUsuario.setInt(1, idUsuario);
            stmtUsuario.executeUpdate();
        }
    }
}
