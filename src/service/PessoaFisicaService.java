package service;

import dao.PessoaFisicaDAO;
import model.PessoaFisica;

import java.sql.Connection;
import java.sql.SQLException;

public class PessoaFisicaService {
    private PessoaFisicaDAO dao;

    public PessoaFisicaService(Connection conexao) throws SQLException {
        this.dao = new PessoaFisicaDAO(conexao);
    }

    public void salvar(PessoaFisica pf) throws SQLException {
        // Aqui você pode colocar validações antes
        if (pf.getNome() == null || pf.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        dao.inserir(pf);
    }
}

