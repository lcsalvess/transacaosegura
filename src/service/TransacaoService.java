package service;

import dao.TransacaoDAO;
import model.Estabelecimento;
import model.Transacao;

import java.sql.Connection;
import java.sql.SQLException;

public class TransacaoService {
    private final TransacaoDAO transacaoDAO;
    private final EstabelecimentoService estabelecimentoService;
    public TransacaoService(Connection conexao) throws SQLException {
        this.transacaoDAO = new TransacaoDAO(conexao);
        this.estabelecimentoService = new EstabelecimentoService(conexao);
    }

    public void salvarTransacao(Transacao transacao) throws SQLException {
        transacaoDAO.inserir(transacao);
    }

    public Estabelecimento obterOuCriarEstabelecimento(String nome) throws SQLException {
        Estabelecimento est = estabelecimentoService.buscarPorNome(nome);
        if (est == null) {
            est = new Estabelecimento(nome);
            estabelecimentoService.salvarEstabelecimento(est);  // define o id dentro do objeto
        }
        return est;
    }
}
