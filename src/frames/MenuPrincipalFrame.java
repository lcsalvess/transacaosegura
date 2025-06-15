package frames;

import controller.TransacaoController;
import model.Usuario;
import service.UsuarioService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuPrincipalFrame extends JFrame {
    private static final Logger logger = Logger.getLogger(MenuPrincipalFrame.class.getName());
    private final Connection conexao;

    public MenuPrincipalFrame(Connection conexao) {
        this.conexao = conexao;

        setTitle("Menu Principal");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel com layout vertical
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(5, 1, 20, 20));
        painel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        painel.setBackground(Color.decode("#f0f0f0")); // Fundo suave

        // Botões estilizados
        JButton btnCadastro = criarBotao("Cadastro");
        JButton btnConsulta = criarBotao("Consulta");
        JButton btnAtualizacao = criarBotao("Atualização do Cadastro");
        JButton btnTransacoes = criarBotao("Transações");
        JButton btnDeletar = criarBotao("Deletar Cadastro");


        // Ações dos botões
        btnCadastro.addActionListener(this::abrirCadastro);
        btnConsulta.addActionListener(this::abrirConsulta);
        btnAtualizacao.addActionListener(this::atualizarCadastro);
        btnTransacoes.addActionListener(this::abrirTransacoes);
        btnDeletar.addActionListener(this::deletarCadastro);


        // Adiciona ao painel
        painel.add(btnCadastro);
        painel.add(btnConsulta);
        painel.add(btnAtualizacao);
        painel.add(btnTransacoes);
        painel.add(btnDeletar);

        add(painel);
    }

    // Cria botão estilizado
    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFocusPainted(false); // Remove contorno de foco
        botao.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Fonte moderna e grande
        botao.setBackground(new Color(76, 175, 80)); // Verde estilizado (#4CAF50)
        botao.setForeground(Color.WHITE); // Letra branca
        botao.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2, true)); // Borda arredondada
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor tipo "mão"
        return botao;
    }

    private void abrirCadastro(ActionEvent e) {
        try {
            new CadastroFrame(conexao).setVisible(true);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao abrir tela de cadastro", ex);
            JOptionPane.showMessageDialog(this, "Erro ao abrir tela de cadastro: " + ex.getMessage());
        }
    }

    private void abrirConsulta(ActionEvent e) {
        try {
            new ConsultaCadastroFrame(conexao).setVisible(true);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao abrir tela de consulta", ex);
            JOptionPane.showMessageDialog(this, "Erro ao abrir tela de consulta: " + ex.getMessage());
        }
    }

    private void atualizarCadastro(ActionEvent e) {
        try {
            new AtualizarCadastroFrame(conexao).setVisible(true);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao abrir tela de atualização", ex);
            JOptionPane.showMessageDialog(this, "Erro ao abrir tela de atualização: " + ex.getMessage());
        }
    }

    private void abrirTransacoes(ActionEvent e) {
        try {
            UsuarioService usuarioService = new UsuarioService(conexao);
            TransacaoController transacaoController = new TransacaoController(conexao, usuarioService);
            transacaoController.iniciarFluxo();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Erro ao iniciar transação", ex);
            JOptionPane.showMessageDialog(this,
                    "Erro ao iniciar transação: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletarCadastro(ActionEvent e) {
        try {
            new DeletarCadastroFrame(conexao).setVisible(true);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao abrir tela de deletar cadastro", ex);
            JOptionPane.showMessageDialog(this, "Erro ao abrir tela de deletar cadastro: " + ex.getMessage());
        }
    }
}
