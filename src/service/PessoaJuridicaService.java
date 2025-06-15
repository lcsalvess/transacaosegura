package service;

import dao.PessoaJuridicaDAO;
import model.PessoaJuridica;

import java.sql.Connection;
import java.sql.SQLException;

public class PessoaJuridicaService {

    private final PessoaJuridicaDAO pessoaJuridicaDAO;

    public PessoaJuridicaService(Connection conexao) {
        this.pessoaJuridicaDAO = new PessoaJuridicaDAO(conexao);
    }

    public void salvarPessoaJuridica(PessoaJuridica pj) throws SQLException {
        pessoaJuridicaDAO.inserir(pj);
    }

    public PessoaJuridica buscarPorCnpj(String cnpj) throws Exception {
        return pessoaJuridicaDAO.buscarPorCnpj(cnpj);
    }

    public void atualizarPessoaJuridica(PessoaJuridica pj) throws SQLException {
        pessoaJuridicaDAO.atualizar(pj);
    }
}
