package frames;

import model.Usuario;
import service.PessoaFisicaService;
import service.PessoaJuridicaService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.util.function.Consumer;

public class BuscarClienteFrame extends JFrame {
    private final Connection conexao;
    private final PessoaFisicaService pessoaFisicaService;
    private final PessoaJuridicaService pessoaJuridicaService;
    private final Consumer<Usuario> onClienteEncontrado;

    private JComboBox<String> comboTipoPessoa;
    private JFormattedTextField cpfField;
    private JFormattedTextField cnpjField;
    private JButton btnBuscar;

    public BuscarClienteFrame(Connection conexao,PessoaFisicaService pessoaFisicaService,
                              PessoaJuridicaService pessoaJuridicaService, Consumer<Usuario> onClienteEncontrado) {
        this.conexao = conexao;
        this.pessoaFisicaService = pessoaFisicaService;
        this.pessoaJuridicaService = pessoaJuridicaService;
        this.onClienteEncontrado = onClienteEncontrado;

        setTitle("Buscar Cliente por CPF/CNPJ");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel painel = new JPanel();
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        // Combo para selecionar tipo de pessoa
        comboTipoPessoa = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});
        comboTipoPessoa.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboTipoPessoa.addActionListener(e -> atualizarCampoPorTipoPessoa());

        // Cria campos com máscara
        cpfField = criarCampoComMascara("###.###.###-##");
        cnpjField = criarCampoComMascara("##.###.###/####-##");

        cpfField.setAlignmentX(Component.LEFT_ALIGNMENT);
        cnpjField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Inicialmente mostra CPF, esconde CNPJ
        cpfField.setVisible(true);
        cnpjField.setVisible(false);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBuscar.addActionListener(this::onBuscarClicked);

        // Rótulos
        JLabel labelTipo = new JLabel("Tipo de Pessoa:");
        JLabel labelCpf = new JLabel("CPF:");
        JLabel labelCnpj = new JLabel("CNPJ:");

        labelTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelCpf.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelCnpj.setAlignmentX(Component.LEFT_ALIGNMENT);

        painel.add(labelTipo);
        painel.add(comboTipoPessoa);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));

        painel.add(labelCpf);
        painel.add(cpfField);

        painel.add(labelCnpj);
        painel.add(cnpjField);

        painel.add(Box.createRigidArea(new Dimension(0, 20)));
        painel.add(btnBuscar);

        add(painel);
    }

    private void atualizarCampoPorTipoPessoa() {
        String tipo = (String) comboTipoPessoa.getSelectedItem();
        if ("Pessoa Física".equals(tipo)) {
            cpfField.setVisible(true);
            cnpjField.setVisible(false);
        } else {
            cpfField.setVisible(false);
            cnpjField.setVisible(true);
        }
        revalidate();
        repaint();
    }

    private void onBuscarClicked(ActionEvent e) {
        String tipo = (String) comboTipoPessoa.getSelectedItem();
        String valorPesquisa;

        if ("Pessoa Física".equals(tipo)) {
            valorPesquisa = cpfField.getText().replaceAll("\\D", ""); // tira pontos e traços
            if (valorPesquisa.isEmpty() || valorPesquisa.contains("_")) {
                JOptionPane.showMessageDialog(this, "Digite um CPF válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            buscarClientePorCpf(valorPesquisa);
        } else {
            valorPesquisa = cnpjField.getText().replaceAll("\\D", ""); // tira pontos, barras e traços
            if (valorPesquisa.isEmpty() || valorPesquisa.contains("_")) {
                JOptionPane.showMessageDialog(this, "Digite um CNPJ válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            buscarClientePorCnpj(valorPesquisa);
        }
    }

    private void buscarClientePorCpf(String cpf) {
        try {
            Integer idUsuario = pessoaFisicaService.buscarIdPorCpf(cpf);
            if (idUsuario != null) {
                Usuario usuario = new Usuario();
                usuario.setId(idUsuario);  // armazena só o ID
                onClienteEncontrado.accept(usuario);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void buscarClientePorCnpj(String cnpj) {
        try {
            Integer idUsuario = pessoaJuridicaService.buscarIdPorCnpj(cnpj);
            if (idUsuario != null) {
                Usuario usuario = new Usuario();
                usuario.setId(idUsuario);  // só armazenar o ID mesmo
                onClienteEncontrado.accept(usuario);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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
