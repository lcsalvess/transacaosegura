package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import transacaosegura.PessoaFisica;
import transacaosegura.PessoaJuridica;
import transacaosegura.Usuario;

public class CadastroFrame extends JFrame {
    private JTextField nomeField;
    private JTextField celularField;

    // Campos para CPF/CNPJ
    private JTextField cpfField;
    private JTextField cnpjField;
    private JTextField razaoSocialField;

    // Botões de rádio para escolher o tipo
    private JRadioButton rbPessoaFisica;
    private JRadioButton rbPessoaJuridica;

    // Painéis para agrupar campos específicos e controlar visibilidade
    private JPanel pessoaFisicaPanel;
    private JPanel pessoaJuridicaPanel;

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
        celularField = new JTextField();
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
        cpfField = new JTextField();
        pessoaFisicaPanel.add(cpfField);
        mainPanel.add(pessoaFisicaPanel);

        // --- Painel para campos de Pessoa Jurídica (inicialmente oculto) ---
        pessoaJuridicaPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        pessoaJuridicaPanel.add(new JLabel("CNPJ:"));
        cnpjField = new JTextField();
        pessoaJuridicaPanel.add(cnpjField);
        pessoaJuridicaPanel.add(new JLabel("Razão Social:"));
        razaoSocialField = new JTextField();
        pessoaJuridicaPanel.add(razaoSocialField);
        pessoaJuridicaPanel.setVisible(false); // Esconde no início
        mainPanel.add(pessoaJuridicaPanel);

        // --- Listener para os botões de rádio (mostrar/esconder campos) ---
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rbPessoaFisica.isSelected()) {
                    pessoaFisicaPanel.setVisible(true);
                    pessoaJuridicaPanel.setVisible(false);
                } else { // rbPessoaJuridica.isSelected()
                    pessoaFisicaPanel.setVisible(false);
                    pessoaJuridicaPanel.setVisible(true);
                }
                // Ajusta o tamanho da janela para o novo conteúdo
                pack(); // Redimensiona a janela para se ajustar aos componentes
            }
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
        cadastrarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText().trim();
                String celular = celularField.getText().trim();
                Usuario user = null; // O objeto user será PessoaFisica ou PessoaJuridica

                if (nome.isEmpty() || celular.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nome e Número Celular são obrigatórios!");
                    return; // Sai da função
                }

                if (rbPessoaFisica.isSelected()) {
                    String cpf = cpfField.getText().trim();
                    if (cpf.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "CPF é obrigatório para Pessoa Física!");
                        return;
                    }
                    user = new PessoaFisica(nome, celular, cpf);
                } else { // rbPessoaJuridica.isSelected()
                    String cnpj = cnpjField.getText().trim();
                    String razaoSocial = razaoSocialField.getText().trim();
                    if (cnpj.isEmpty() || razaoSocial.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "CNPJ e Razão Social são obrigatórios para Pessoa Jurídica!");
                        return;
                    }
                    user = new PessoaJuridica(nome, celular, cnpj, razaoSocial);
                }

                // Se o usuário foi criado com sucesso
                if (user != null) {
                    JOptionPane.showMessageDialog(null, "Usuário criado com sucesso!\n" + user.toString());
                    dispose(); // fecha o frame atual
                    new ValorCompraFrame(user).setVisible(true); // Abre a próxima tela
                }
            }
        });
    }
}