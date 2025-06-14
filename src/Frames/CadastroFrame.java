package Frames;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;

import transacaosegura.PessoaFisica;
import transacaosegura.PessoaJuridica;
import transacaosegura.Usuario;

public class CadastroFrame extends JFrame {
    private final JTextField nomeField;
    private JTextField celularField;

    // Campos para CPF/CNPJ
    private JTextField cpfField;
    private JTextField cnpjField;
    private final JTextField razaoSocialField;

    // Botões de rádio para escolher o tipo
    private final JRadioButton rbPessoaFisica;
    private final JRadioButton rbPessoaJuridica;

    // Painéis para agrupar campos específicos e controlar visibilidade
    private final JPanel pessoaFisicaPanel;
    private final JPanel pessoaJuridicaPanel;

    public CadastroFrame() {
        setTitle("Cadastro de Usuário");
        setSize(400, 350); // Aumenta o tamanho para acomodar os novos campos
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Layout vertical
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Margem interna

        // --- Painel para Nome e Celular ---
        JPanel commonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        commonPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        commonPanel.add(nomeField);
        commonPanel.add(new JLabel("Número Celular:"));
        // celularField = new JTextField();
        try {
            MaskFormatter telefoneMask = new MaskFormatter("(##) #####-####");
            telefoneMask.setPlaceholderCharacter('_');
            celularField = new JFormattedTextField(telefoneMask);
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(null, "Erro ao aplicar máscara de telefone!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        commonPanel.add(celularField);
        mainPanel.add(commonPanel);

        mainPanel.add(Box.createVerticalStrut(15)); // Espaçamento

        // --- Painel para escolha de Tipo (Pessoa Física / Jurídica) ---
        JPanel typeSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        typeSelectionPanel.add(new JLabel("Tipo de Usuário:"));

        rbPessoaFisica = new JRadioButton("Pessoa Física");
        rbPessoaJuridica = new JRadioButton("Pessoa Jurídica");

        ButtonGroup group = new ButtonGroup(); // Garante que apenas um pode ser selecionado
        group.add(rbPessoaFisica);
        group.add(rbPessoaJuridica);

        // Seleciona Pessoa Física por padrão
        rbPessoaFisica.setSelected(true);

        typeSelectionPanel.add(rbPessoaFisica);
        typeSelectionPanel.add(rbPessoaJuridica);
        mainPanel.add(typeSelectionPanel);

        mainPanel.add(Box.createVerticalStrut(10)); // Espaçamento

        // --- Painel para campos de Pessoa Física (inicialmente visível) ---
        pessoaFisicaPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        pessoaFisicaPanel.add(new JLabel("CPF:"));
        // cpfField = new JTextField();
        try {
            cpfField = new JFormattedTextField(new MaskFormatter("###.###.###-##"));
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(null, "Erro ao aplicar máscara de CPF!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        pessoaFisicaPanel.add(cpfField);
        mainPanel.add(pessoaFisicaPanel);

        // --- Painel para campos de Pessoa Jurídica (inicialmente oculto) ---
        pessoaJuridicaPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        pessoaJuridicaPanel.add(new JLabel("CNPJ:"));
        //cnpjField = new JTextField();
        try {
            cnpjField = new JFormattedTextField(new MaskFormatter("##.###.###/####-##"));
        } catch (java.text.ParseException e) {
            JOptionPane.showMessageDialog(null, "Erro ao aplicar máscara de CNPJ!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        pessoaJuridicaPanel.add(cnpjField);
        pessoaJuridicaPanel.add(new JLabel("Razão Social:"));
        razaoSocialField = new JTextField();
        pessoaJuridicaPanel.add(razaoSocialField);
        pessoaJuridicaPanel.setVisible(false); // Esconde no início
        mainPanel.add(pessoaJuridicaPanel);

        // --- Listener para os botões de rádio (mostrar/esconder campos) ---
        ActionListener radioListener = e -> {
            if (rbPessoaFisica.isSelected()) {
                pessoaFisicaPanel.setVisible(true);
                pessoaJuridicaPanel.setVisible(false);
            } else {
                pessoaFisicaPanel.setVisible(false);
                pessoaJuridicaPanel.setVisible(true);
            }
            pack(); //Redimensiona a janela para se ajustar aos componentes
        };
        rbPessoaFisica.addActionListener(radioListener);
        rbPessoaJuridica.addActionListener(radioListener);


        mainPanel.add(Box.createVerticalStrut(20)); // Espaçamento

        // --- Botão Cadastrar ---
        JButton cadastrarBtn = new JButton("Cadastrar");
        cadastrarBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o botão
        mainPanel.add(cadastrarBtn);

        add(mainPanel); // Adiciona o painel principal ao frame

        // Ajusta o tamanho inicial da janela
        pack();


        // --- Ação do botão Cadastrar ---
        cadastrarBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String celular = celularField.getText().replaceAll("\\D", "");

            if (nome.isEmpty() || celular.length() != 11) {
                JOptionPane.showMessageDialog(null, "Nome e número de celular são obrigatórios", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (rbPessoaFisica.isSelected()) {
                String cpf = cpfField.getText().replaceAll("\\D", "");
                if (cpf.length() != 11) {
                    JOptionPane.showMessageDialog(null, "CPF deve conter 11 dígitos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Usuario user = new PessoaFisica(nome, celular, cpf);
                JOptionPane.showMessageDialog(null, "Usuário criado com sucesso!\n" + user);
                dispose();
                new ValorCompraFrame(user).setVisible(true);
            } else {
                String cnpj = cnpjField.getText().replaceAll("\\D", "");
                String razaoSocial = razaoSocialField.getText().trim();
                if (cnpj.length() != 14 || razaoSocial.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "CNPJ deve conter 14 dígitos e Razão Social é obrigatória", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Usuario user = new PessoaJuridica(nome, celular, cnpj, razaoSocial);
                JOptionPane.showMessageDialog(null, "Usuário criado com sucesso!\n" + user);
                dispose();
                new ValorCompraFrame(user).setVisible(true);
            }
        });
    }
}