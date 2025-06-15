package controller;

import dao.EstabelecimentoDAO;
import frames.ConfirmarTransacaoFrame;
import frames.ValorCompraFrame;
import model.Estabelecimento;
import model.Transacao;
import model.Usuario;
import service.TransacaoService;
import service.UsuarioService;
import util.SystemInfo;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class TransacaoController {

    private final Connection conexao;
    private final TransacaoService transacaoService;
    private final UsuarioService usuarioService;

    public TransacaoController(Connection conexao, UsuarioService usuarioService) throws SQLException {
        this.conexao = conexao;
        this.transacaoService = new TransacaoService(conexao);
        this.usuarioService = usuarioService;
    }

    public void iniciarFluxo() {
        // Primeiro passo: pedir CPF/CNPJ para buscar usuário
        SwingUtilities.invokeLater(() -> {
            String cpfOuCnpj = JOptionPane.showInputDialog(null,
                    "Digite o CPF ou CNPJ do usuário:",
                    "Buscar Usuário",
                    JOptionPane.QUESTION_MESSAGE);

            if (cpfOuCnpj == null || cpfOuCnpj.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "CPF/CNPJ inválido ou não informado.");
                return;
            }

            try {
                Usuario usuario = usuarioService.buscarPorCpfOuCnpj(cpfOuCnpj.trim());
                if (usuario == null) {
                    JOptionPane.showMessageDialog(null,
                            "Usuário não encontrado para o CPF/CNPJ informado.",
                            "Usuário não encontrado",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                abrirFrameValorCompra(usuario);

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erro ao buscar usuário: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void abrirFrameValorCompra(Usuario usuario) {
        SwingUtilities.invokeLater(() -> new ValorCompraFrame(usuario, this).setVisible(true));
    }

    public void processarValorCompra(Usuario usuario, double valorCompra) {
        try {
            String nomeEstabelecimento = SystemInfo.getNomeComputador(); // ou qualquer outro identificador
            EstabelecimentoDAO estabelecimentoDAO = new EstabelecimentoDAO(conexao);

            // Verifica se já existe no banco
            Estabelecimento estabelecimento = estabelecimentoDAO.buscarPorNome(nomeEstabelecimento);

            if (estabelecimento == null) {
                // Se não existir, insere e obtém o id gerado
                estabelecimento = new Estabelecimento(nomeEstabelecimento);
                estabelecimentoDAO.inserir(estabelecimento);
            }

            Transacao transacao = new Transacao(usuario.getId(), valorCompra, estabelecimento);

            if (transacao.isConfirmacaoNecessaria()) {
                SwingUtilities.invokeLater(() -> new ConfirmarTransacaoFrame(usuario, valorCompra, confirmado -> {
                    if (confirmado) {
                        transacao.setStatus(Transacao.Status.APROVADA);
                        salvarTransacao(transacao);
                        JOptionPane.showMessageDialog(null, "Transação confirmada e salva com sucesso!");
                    } else {
                        transacao.setStatus(Transacao.Status.RECUSADA);
                        JOptionPane.showMessageDialog(null, "Transação foi recusada.");
                    }
                }).setVisible(true));
            } else {
                transacao.setStatus(Transacao.Status.APROVADA);
                salvarTransacao(transacao);
                JOptionPane.showMessageDialog(null, "Compra aprovada automaticamente e salva.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Erro ao processar transação: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarTransacao(Transacao transacao) {
        try {
            transacaoService.salvarTransacao(transacao);
            System.out.println("Transação salva no banco: " + transacao);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Erro ao salvar transação: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}