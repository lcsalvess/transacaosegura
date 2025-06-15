package service;

import dao.EstabelecimentoDAO;
import model.Estabelecimento;

import java.sql.Connection;
import java.sql.SQLException;

public class EstabelecimentoService {

    private final EstabelecimentoDAO estabelecimentoDAO;

    public EstabelecimentoService(Connection conexao) throws SQLException {
        this.estabelecimentoDAO = new EstabelecimentoDAO(conexao);
    }

    public int salvarEstabelecimento(Estabelecimento estabelecimento) throws SQLException {
        return estabelecimentoDAO.inserir(estabelecimento);
    }

    public Estabelecimento buscarPorId(int id) throws SQLException {
        return estabelecimentoDAO.buscarPorId(id);
    }

    public Estabelecimento buscarPorNome(String nome) throws SQLException {
        return estabelecimentoDAO.buscarPorNome(nome);
    }
}
