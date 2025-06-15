package dao;

import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private final Connection conexao;

    public UsuarioDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, numero_celular, ativo FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setNumeroCelular(rs.getString("numero_celular"));
                    usuario.setAtivo(rs.getBoolean("ativo"));
                    return usuario;
                }
            }
        }
        return null; // não encontrado
    }

    public void desativarUsuario(Integer idUsuario) throws SQLException {
        String sql = "UPDATE usuario SET ativo = FALSE WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Usuário com id " + idUsuario + " não encontrado.");
            }
        }
    }
}
