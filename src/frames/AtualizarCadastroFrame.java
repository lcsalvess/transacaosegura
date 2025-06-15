package frames;

import model.PessoaFisica;
import model.PessoaJuridica;
import service.PessoaFisicaService;
import service.PessoaJuridicaService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AtualizarCadastroFrame extends JFrame {
    private static final Logger logger = Logger.getLogger(AtualizarCadastroFrame.class.getName());

    private final Connection conexao;
    private final PessoaFisicaService pfService;
    private final PessoaJuridicaService pjService;

    private final JRadioButton rbPessoaFisica;
    private final JRadioButton rbPessoaJuridica;

    private final JFormattedTextField cpfField;
    private final JFormattedTextField cnpjField;

    private final JPanel cpfPanel;
    private final JPanel cnpjPanel;

    private final JPanel nomePanel;
    private final JPanel razaoSocialPanel;

    private final JTextField nomeField;       // Para pessoa física
    private final JTextField razaoSocialField; // Para pessoa jurídica
    private final JTextField celularField;

    private final JButton buscarButton;
    private final JButton salvarButton;

    public AtualizarCadastroFrame(Connection conexao) throws SQLException {
        this.conexao = conexao;
        this.pfService = new PessoaFisicaService(conexao);
        this.pjService = new PessoaJuridicaService(conexao);

        setTitle("Atualizar Cadastro");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel tipoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tipoPanel.add(new JLabel("Tipo de Pessoa:"));

        rbPessoaFisica = new JRadioButton("Pessoa Física");
        rbPessoaJuridica = new JRadioButton("Pessoa Jurídica");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbPessoaFisica);
        grupoTipo.add(rbPessoaJuridica);
        rbPessoaFisica.setSelected(true);

        tipoPanel.add(rbPessoaFisica);
        tipoPanel.add(rbPessoaJuridica);
        mainPanel.add(tipoPanel);

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

        nomeField = new JTextField();
        razaoSocialField = new JTextField();

        nomePanel = new JPanel(new BorderLayout());
        nomePanel.add(new JLabel("Nome:"), BorderLayout.WEST);
        nomePanel.add(nomeField, BorderLayout.CENTER);

        razaoSocialPanel = new JPanel(new BorderLayout());
        razaoSocialPanel.add(new JLabel("Razão Social:"), BorderLayout.WEST);
        razaoSocialPanel.add(razaoSocialField, BorderLayout.CENTER);

        celularField = criarCampoComMascara("(##) #####-####");

        JPanel celularPanel = new JPanel(new BorderLayout());
        celularPanel.add(new JLabel("Celular:"), BorderLayout.WEST);
        celularPanel.add(celularField, BorderLayout.CENTER);

        mainPanel.add(nomePanel);
        mainPanel.add(razaoSocialPanel);
        mainPanel.add(celularPanel);

        nomeField.setEnabled(false);
        razaoSocialField.setEnabled(false);
        celularField.setEnabled(false);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buscarButton = new JButton("Buscar");
        salvarButton = new JButton("Salvar");
        salvarButton.setEnabled(false);

        buttonsPanel.add(buscarButton);
        buttonsPanel.add(salvarButton);

        mainPanel.add(buttonsPanel);
        add(mainPanel);
        pack();

        ActionListener tipoListener = e -> {
            boolean pessoaFisica = rbPessoaFisica.isSelected();
            cpfPanel.setVisible(pessoaFisica);
            cnpjPanel.setVisible(!pessoaFisica);
            nomePanel.setVisible(pessoaFisica);
            razaoSocialPanel.setVisible(!pessoaFisica);

            nomeField.setEnabled(false);
            razaoSocialField.setEnabled(false);
            celularField.setEnabled(false);

            limparCampos();
            salvarButton.setEnabled(false);
            pack();
        };

        rbPessoaFisica.addActionListener(tipoListener);
        rbPessoaJuridica.addActionListener(tipoListener);

        buscarButton.addActionListener(e -> buscarCadastro());
        salvarButton.addActionListener(e -> salvarAtualizacao());

        tipoListener.actionPerformed(null);
    }

    private void limparCampos() {
        nomeField.setText("");
        razaoSocialField.setText("");
        celularField.setText("");
        cpfField.setText("");
        cnpjField.setText("");
    }

    private void buscarCadastro() {
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
                nomeField.setText(pf.getNome());
                celularField.setText(pf.getNumeroCelular());
                nomeField.setEnabled(true);
                celularField.setEnabled(true);
                salvarButton.setEnabled(true);
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
                razaoSocialField.setText(pj.getRazaoSocial());
                celularField.setText(pj.getNumeroCelular());
                razaoSocialField.setEnabled(true);
                celularField.setEnabled(true);
                salvarButton.setEnabled(true);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro na busca", ex);
            JOptionPane.showMessageDialog(this, "Erro na busca: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarAtualizacao() {
        try {
            if (rbPessoaFisica.isSelected()) {
                String cpf = cpfField.getText().replaceAll("\\D", "");
                String nome = nomeField.getText().trim();
                String celular = celularField.getText().trim();
                if (nome.isEmpty() || celular.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome e celular não podem ficar vazios.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                PessoaFisica pf = new PessoaFisica(nome, celular, cpf);
                pfService.atualizar(pf);
                JOptionPane.showMessageDialog(this, "Pessoa Física atualizada com sucesso!");
            } else {
                String cnpj = cnpjField.getText().replaceAll("\\D", "");
                String razaoSocial = razaoSocialField.getText().trim();
                String celular = celularField.getText().trim();
                if (razaoSocial.isEmpty() || celular.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Razão Social e celular não podem ficar vazios.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                PessoaJuridica pj = new PessoaJuridica(null, celular, cnpj, razaoSocial);
                pjService.atualizarPessoaJuridica(pj);
                JOptionPane.showMessageDialog(this, "Pessoa Jurídica atualizada com sucesso!");
            }
            limparCampos();
            salvarButton.setEnabled(false);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao atualizar", ex);
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

