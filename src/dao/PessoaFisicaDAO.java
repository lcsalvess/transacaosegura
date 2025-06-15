package dao;

import model.PessoaFisica;

import java.sql.*;

public class PessoaFisicaDAO {
    private final Connection conexao;

    public PessoaFisicaDAO(Connection conexao) {
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

    public PessoaFisica buscarPorCpf(String cpf) throws Exception {
        String sql = "SELECT u.nome, u.NUMERO_CELULAR, pf.cpf " +
                "FROM pessoa_fisica pf " +
                "JOIN usuario u ON pf.ID_USUARIO = u.id " +
                "WHERE pf.cpf = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new PessoaFisica(
                        rs.getString("nome"),
                        rs.getString("numero_celular"),
                        rs.getString("cpf")
                );
            }
            return null;
        }
    }

    public void atualizar(PessoaFisica pf) throws SQLException {
        // Primeiro, recuperar o ID do usuário com base no CPF
        String sqlBuscarId = "SELECT id_usuario FROM pessoa_fisica WHERE cpf = ?";
        int idUsuario;

        try (PreparedStatement stmtBuscar = conexao.prepareStatement(sqlBuscarId)) {
            stmtBuscar.setString(1, pf.getCpf());
            ResultSet rs = stmtBuscar.executeQuery();
            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario");
            } else {
                throw new SQLException("CPF não encontrado.");
            }
        }

        // Agora, atualizar os dados na tabela usuario
        String sqlAtualizarUsuario = "UPDATE usuario SET nome = ?, numero_celular = ? WHERE id = ?";
        try (PreparedStatement stmtAtualizar = conexao.prepareStatement(sqlAtualizarUsuario)) {
            stmtAtualizar.setString(1, pf.getNome());
            stmtAtualizar.setString(2, pf.getNumeroCelular());
            stmtAtualizar.setInt(3, idUsuario);
            int linhasAfetadas = stmtAtualizar.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar os dados. Nenhum registro afetado.");
            }
        }
    }

    public void deletarPorCpf(PessoaFisica pf) throws SQLException {
        // Buscar o ID do usuário pelo CPF
        String sqlBuscarId = "SELECT id_usuario FROM pessoa_fisica WHERE cpf = ?";
        int idUsuario;

        try (PreparedStatement stmtBuscar = conexao.prepareStatement(sqlBuscarId)) {
            stmtBuscar.setString(1, pf.getCpf());
            ResultSet rs = stmtBuscar.executeQuery();
            if (rs.next()) {
                idUsuario = rs.getInt("id_usuario");
            } else {
                throw new SQLException("CPF não encontrado.");
            }
        }

        // Deletar da tabela pessoa_fisica
        String sqlDeletarPF = "DELETE FROM pessoa_fisica WHERE cpf = ?";
        try (PreparedStatement stmtPF = conexao.prepareStatement(sqlDeletarPF)) {
            stmtPF.setString(1, pf.getCpf());
            stmtPF.executeUpdate();
        }

        // Deletar da tabela usuario
        String sqlDeletarUsuario = "DELETE FROM usuario WHERE id = ?";
        try (PreparedStatement stmtUsuario = conexao.prepareStatement(sqlDeletarUsuario)) {
            stmtUsuario.setInt(1, idUsuario);
            stmtUsuario.executeUpdate();
        }
    }
}
