package frames;

import model.PessoaFisica;
import model.PessoaJuridica;
import service.PessoaFisicaService;
import service.PessoaJuridicaService;
import service.UsuarioService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeletarCadastroFrame extends JFrame {
    private static final Logger logger = Logger.getLogger(DeletarCadastroFrame.class.getName());

    private final PessoaFisicaService pfService;
    private final PessoaJuridicaService pjService;

    private final JRadioButton rbPessoaFisica;
    private final JRadioButton rbPessoaJuridica;

    private final JFormattedTextField cpfField;
    private final JFormattedTextField cnpjField;

    private final JPanel cpfPanel;
    private final JPanel cnpjPanel;

    private final JButton deletarButton;

    private final UsuarioService usuarioService;

    public DeletarCadastroFrame(Connection conexao, UsuarioService usuarioService) throws SQLException {
        this.usuarioService = usuarioService;
        this.pfService = new PessoaFisicaService(conexao);
        this.pjService = new PessoaJuridicaService(conexao);

        setTitle("Deletar Cadastro");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tipo de pessoa
        JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tipoPanel.add(new JLabel("Tipo de Pessoa:"));

        rbPessoaFisica = new JRadioButton("Pessoa Física", true);
        rbPessoaJuridica = new JRadioButton("Pessoa Jurídica");

        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbPessoaFisica);
        grupoTipo.add(rbPessoaJuridica);

        tipoPanel.add(rbPessoaFisica);
        tipoPanel.add(rbPessoaJuridica);
        mainPanel.add(tipoPanel);

        // Campos de CPF/CNPJ
        cpfField = criarCampoComMascara("###.###.###-##");
        cnpjField = criarCampoComMascara("##.###.###/####-##");

        cpfPanel = new JPanel(new BorderLayout());
        cpfPanel.add(new JLabel("CPF:"), BorderLayout.WEST);
        cpfPanel.add(cpfField, BorderLayout.CENTER);

        cnpjPanel = new JPanel(new BorderLayout());
        cnpjPanel.add(new JLabel("CNPJ:"), BorderLayout.WEST);
        cnpjPanel.add(cnpjField, BorderLayout.CENTER);

        mainPanel.add(cpfPanel);
        mainPanel.add(cnpjPanel);

        deletarButton = new JButton("Deletar");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(deletarButton);
        mainPanel.add(buttonPanel);

        add(mainPanel);
        pack();

        ActionListener tipoListener = e -> {
            boolean fisica = rbPessoaFisica.isSelected();
            cpfPanel.setVisible(fisica);
            cnpjPanel.setVisible(!fisica);
            pack();
        };

        rbPessoaFisica.addActionListener(tipoListener);
        rbPessoaJuridica.addActionListener(tipoListener);

        deletarButton.addActionListener(e -> deletarUsuario());

        tipoListener.actionPerformed(null);
    }

    private void deletarUsuario() {
        try {
            if (rbPessoaFisica.isSelected()) {
                String cpf = cpfField.getText().replaceAll("\\D", "");
                if (cpf.length() != 11) {
                    JOptionPane.showMessageDialog(this, "CPF inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PessoaFisica pf = pfService.buscarPorCpf(cpf);
                if (pf == null) {
                    JOptionPane.showMessageDialog(this, "Pessoa Física não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Você tem certeza que deseja desativar o usuário: " + pf.getNome() + "?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    usuarioService.desativarUsuarioPorCpfOuCnpj(cpf);
                    JOptionPane.showMessageDialog(this, "Pessoa Física desativada com sucesso!");
                }

            } else {
                String cnpj = cnpjField.getText().replaceAll("\\D", "");
                if (cnpj.length() != 14) {
                    JOptionPane.showMessageDialog(this, "CNPJ inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PessoaJuridica pj = pjService.buscarPorCnpj(cnpj);
                if (pj == null) {
                    JOptionPane.showMessageDialog(this, "Pessoa Jurídica não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Você tem certeza que deseja desativar a empresa: " + pj.getRazaoSocial() + "?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    usuarioService.desativarUsuarioPorCpfOuCnpj(cnpj);
                    JOptionPane.showMessageDialog(this, "Pessoa Jurídica desativada com sucesso!");
                }
            }

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao desativar", ex);
            JOptionPane.showMessageDialog(this, "Erro ao desativar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JFormattedTextField criarCampoComMascara(String mascara) {
        try {
            MaskFormatter formatter = new MaskFormatter(mascara);
            formatter.setPlaceholderCharacter('_');
            return new JFormattedTextField(formatter);
        } catch (java.text.ParseException e) {
            throw new RuntimeException("Erro ao aplicar máscara: " + mascara, e);
        }
    }
}
