package frames;

import model.PessoaFisica;
import model.PessoaJuridica;
import service.PessoaFisicaService;
import service.PessoaJuridicaService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultaCadastroFrame extends JFrame {
    private static final Logger logger = Logger.getLogger(ConsultaCadastroFrame.class.getName());
    private final Connection conexao;

    private final JRadioButton rbFisica;
    private final JRadioButton rbJuridica;

    private final JFormattedTextField cpfField;
    private final JFormattedTextField cnpjField;

    private final PessoaFisicaService pfService;
    private final PessoaJuridicaService pjService;

    private final JPanel inputPanel;
    private final CardLayout cardLayout;

    private final JPanel cpfPanel;
    private final JPanel cnpjPanel;

    public ConsultaCadastroFrame(Connection conexao) throws SQLException {
        this.conexao = conexao;

        pfService = new PessoaFisicaService(conexao);
        pjService = new PessoaJuridicaService(conexao);

        setTitle("Consulta de Cadastro");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Radio buttons
        rbFisica = new JRadioButton("Pessoa Física");
        rbJuridica = new JRadioButton("Pessoa Jurídica");

        ButtonGroup group = new ButtonGroup();
        group.add(rbFisica);
        group.add(rbJuridica);
        rbFisica.setSelected(true);

        JPanel radioPanel = new JPanel(new FlowLayout());
        radioPanel.add(rbFisica);
        radioPanel.add(rbJuridica);

        panel.add(radioPanel, BorderLayout.NORTH);

        // Painel de entrada com CardLayout
        cardLayout = new CardLayout();
        inputPanel = new JPanel(cardLayout);

        // CPF
        cpfPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cpfPanel.add(new JLabel("CPF:"));
        cpfField = criarCampoComMascara("###.###.###-##");
        cpfField.setPreferredSize(new Dimension(150, 25));
        cpfPanel.add(cpfField);

        //CNPJ
        cnpjPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cnpjPanel.add(new JLabel("CNPJ:"));
        cnpjField = criarCampoComMascara("##.###.###/####-##");
        cnpjField.setPreferredSize(new Dimension(150, 25));  // mesmo tamanho para o cnpj
        cnpjPanel.add(cnpjField);
        panel.add(inputPanel, BorderLayout.CENTER);
        // Adiciona os painéis de CPF e CNPJ ao CardLayout
        inputPanel.add(cpfPanel, "FISICA");
        inputPanel.add(cnpjPanel, "JURIDICA");
        panel.add(inputPanel, BorderLayout.CENTER);
        //Botão de consulta
        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(e -> consultar());
        panel.add(btnConsultar, BorderLayout.SOUTH);

        // Atualizar campos com base na seleção
        rbFisica.addActionListener(e -> atualizarCampos());
        rbJuridica.addActionListener(e -> atualizarCampos());

        // Inicialmente: CPF visível, CNPJ oculto
        atualizarCampos();
        add(panel);
        pack();
    }

    private void atualizarCampos() {
        if (rbFisica.isSelected()) {
            cardLayout.show(inputPanel, "FISICA");
        } else {
            cardLayout.show(inputPanel, "JURIDICA");
        }
    }

    private void consultar() {
        try {
            if (rbFisica.isSelected()) {
                String cpf = cpfField.getText().replaceAll("\\D", "");
                if (cpf.length() != 11) {
                    JOptionPane.showMessageDialog(this, "CPF deve conter 11 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                PessoaFisica pf = pfService.buscarPorCpf(cpf);
                if (pf != null) {
                    JOptionPane.showMessageDialog(this,
                            "Pessoa Física encontrada:\n\n" +
                                    "Nome: " + pf.getNome() + "\n" +
                                    "Celular: " + pf.getNumeroCelular() + "\n" +
                                    "CPF: " + pf.getCpf(),
                            "Resultado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Pessoa Física não encontrada.", "Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                String cnpj = cnpjField.getText().replaceAll("\\D", "");
                if (cnpj.length() != 14) {
                    JOptionPane.showMessageDialog(this, "CNPJ deve conter 14 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                PessoaJuridica pj = pjService.buscarPorCnpj(cnpj);
                if (pj != null) {
                    JOptionPane.showMessageDialog(this,
                            "Pessoa Jurídica encontrada:\n\n" +
                                    "Nome: " + pj.getNome() + "\n" +
                                    "Celular: " + pj.getNumeroCelular() + "\n" +
                                    "CNPJ: " + pj.getCnpj() + "\n" +
                                    "Razão Social: " + pj.getRazaoSocial(),
                            "Resultado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Pessoa Jurídica não encontrada.", "Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro na consulta", ex);
            JOptionPane.showMessageDialog(this, "Erro na consulta: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
