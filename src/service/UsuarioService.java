package service;

import dao.PessoaFisicaDAO;
import dao.PessoaJuridicaDAO;
import dao.UsuarioDAO;
import model.PessoaFisica;
import model.PessoaJuridica;
import model.Usuario;

import java.sql.Connection;

public class UsuarioService {

    private final PessoaFisicaDAO pessoaFisicaDAO;
    private final PessoaJuridicaDAO pessoaJuridicaDAO;
    private final UsuarioDAO usuarioDAO;  // Incluído UsuarioDAO

    public UsuarioService(Connection conexao) {
        this.pessoaFisicaDAO = new PessoaFisicaDAO(conexao);
        this.pessoaJuridicaDAO = new PessoaJuridicaDAO(conexao);
        this.usuarioDAO = new UsuarioDAO(conexao);  // Instancia UsuarioDAO
    }

    public Usuario buscarPorCpfOuCnpj(String cpfOuCnpj) throws Exception {
        // Tenta buscar id do usuário na tabela pessoa_fisica
        Integer idUsuario = pessoaFisicaDAO.buscarIdPorCpf(cpfOuCnpj);
        if (idUsuario != null) {
            // Busca o usuário completo pelo id
            return usuarioDAO.buscarPorId(idUsuario);
        }

        // Se não encontrou na pessoa_fisica, tenta buscar na pessoa_juridica
        idUsuario = pessoaJuridicaDAO.buscarIdPorCnpj(cpfOuCnpj);
        if (idUsuario != null) {
            // Busca o usuário completo pelo id
            return usuarioDAO.buscarPorId(idUsuario);
        }

        // Se não encontrou em nenhuma das duas tabelas
        return null;
    }
}