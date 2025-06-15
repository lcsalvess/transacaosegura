package frames;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.PessoaFisica;
import model.PessoaJuridica;
import service.PessoaFisicaService;
import service.PessoaJuridicaService;
import util.RazaoSocialFilter;
import util.SomenteLetrasFilter;

public class CadastroFrame extends JFrame {
    private static final Logger logger = Logger.getLogger(CadastroFrame.class.getName());
    private final Connection conexao;
    private final JTextField nomeField;
    private final JTextField celularField;

    // Campos para CPF/CNPJ
    private final JTextField cpfField;
    private final JTextField cnpjField;
    private final JTextField razaoSocialField;

    // Botões de rádio para escolher o tipo
    private final JRadioButton rbPessoaFisica;
    private final JRadioButton rbPessoaJuridica;

    // Painéis para agrupar campos específicos e controlar visibilidade
    private final JPanel pessoaFisicaPanel;
    private final JPanel pessoaJuridicaPanel;

    //Service
    private final PessoaFisicaService pfService;
    private final PessoaJuridicaService pjService;
    public CadastroFrame(Connection conexao) throws SQLException {
        this.conexao = conexao;

        // Inicializa os services com a conexão
        this.pfService = new PessoaFisicaService(conexao);
        this.pjService = new PessoaJuridicaService(conexao);

        setTitle("Cadastro de Usuário");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel commonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        commonPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        ((AbstractDocument) nomeField.getDocument()).setDocumentFilter(new SomenteLetrasFilter());
        commonPanel.add(nomeField);

        commonPanel.add(new JLabel("Número Celular:"));
        celularField = criarCampoComMascara("(##) #####-####");
        commonPanel.add(celularField);

        mainPanel.add(commonPanel);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel typeSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        typeSelectionPanel.add(new JLabel("Tipo de Usuário:"));

        rbPessoaFisica = new JRadioButton("Pessoa Física");
        rbPessoaJuridica = new JRadioButton("Pessoa Jurídica");

        ButtonGroup group = new ButtonGroup();
        group.add(rbPessoaFisica);
        group.add(rbPessoaJuridica);

        rbPessoaFisica.setSelected(true);

        typeSelectionPanel.add(rbPessoaFisica);
        typeSelectionPanel.add(rbPessoaJuridica);
        mainPanel.add(typeSelectionPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        pessoaFisicaPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        pessoaFisicaPanel.add(new JLabel("CPF:"));
        cpfField = criarCampoComMascara("###.###.###-##");
        pessoaFisicaPanel.add(cpfField);
        mainPanel.add(pessoaFisicaPanel);

        pessoaJuridicaPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        pessoaJuridicaPanel.add(new JLabel("CNPJ:"));
        cnpjField = criarCampoComMascara("##.###.###/####-##");
        pessoaJuridicaPanel.add(cnpjField);
        pessoaJuridicaPanel.add(new JLabel("Razão Social:"));
        razaoSocialField = new JTextField();
        ((AbstractDocument) razaoSocialField.getDocument()).setDocumentFilter(new RazaoSocialFilter());
        pessoaJuridicaPanel.add(razaoSocialField);
        pessoaJuridicaPanel.setVisible(false);
        mainPanel.add(pessoaJuridicaPanel);

        ActionListener radioListener = e -> {
            if (rbPessoaFisica.isSelected()) {
                pessoaFisicaPanel.setVisible(true);
                pessoaJuridicaPanel.setVisible(false);
            } else {
                pessoaFisicaPanel.setVisible(false);
                pessoaJuridicaPanel.setVisible(true);
            }
            pack();
        };
        rbPessoaFisica.addActionListener(radioListener);
        rbPessoaJuridica.addActionListener(radioListener);

        mainPanel.add(Box.createVerticalStrut(20));

        JButton cadastrarBtn = new JButton("Cadastrar");
        cadastrarBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(cadastrarBtn);

        add(mainPanel);
        pack();

        cadastrarBtn.addActionListener(e -> {
            try {
                cadastrarUsuario();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Erro ao cadastrar usuário", ex);
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cadastrarUsuario() throws Exception {
        String nome = nomeField.getText().trim();
        String celular = celularField.getText().replaceAll("\\D", "");

        if (nome.isEmpty() || celular.length() != 11) {
            JOptionPane.showMessageDialog(this, "Nome e número de celular são obrigatórios e devem estar corretos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (rbPessoaFisica.isSelected()) {
            String cpf = cpfField.getText().replaceAll("\\D", "");
            if (cpf.length() != 11) {
                JOptionPane.showMessageDialog(this, "CPF deve conter 11 dígitos!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PessoaFisica pf = new PessoaFisica(nome, celular, cpf);
            pfService.salvar(pf);

            JOptionPane.showMessageDialog(this, "Pessoa Física cadastrada com sucesso!\n" + pf);
        } else {
            String cnpj = cnpjField.getText().replaceAll("\\D", "");
            String razaoSocial = razaoSocialField.getText().trim();

            if (cnpj.length() != 14 || razaoSocial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "CNPJ deve conter 14 dígitos e Razão Social é obrigatória", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PessoaJuridica pj = new PessoaJuridica(nome, celular, cnpj, razaoSocial);
            pjService.salvarPessoaJuridica(pj);

            JOptionPane.showMessageDialog(this, "Pessoa Jurídica cadastrada com sucesso!\n" + pj);
        }

        dispose();
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