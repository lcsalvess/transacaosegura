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

    public PessoaFisica buscarPorCpf(String cpf) throws Exception {
        return dao.buscarPorCpf(cpf);
    }

    public void atualizar(PessoaFisica pf) throws SQLException {
        if (pf.getCpf() == null || pf.getCpf().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório para atualizar");
        }
        if (pf.getNome() == null || pf.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        dao.atualizar(pf);
    }
}

