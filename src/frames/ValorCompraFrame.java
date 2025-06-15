package frames;

import javax.swing.*;
import java.awt.*;

import controller.TransacaoController;
import model.Usuario;
import util.FormatadorNumerico;

public class ValorCompraFrame extends JFrame {
    private final Usuario user;
    private final TransacaoController controller;

    public ValorCompraFrame(Usuario user, TransacaoController controller) {
        this.user = user;
        this.controller = controller;
        // Configurações da janela
        setTitle("Valor da Compra");
        setSize(400, 180);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Digite o valor da compra:");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // Criação de painel com Label "R$" + campo de texto
        JPanel valorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel prefixoReal = new JLabel("R$ ");
        prefixoReal.setVerticalAlignment(SwingConstants.CENTER);
        JFormattedTextField valorField = new JFormattedTextField(FormatadorNumerico.criarNumberFormatter());
        valorField.setColumns(10); // Define o tamanho do campo de texto
        valorPanel.add(prefixoReal);
        valorPanel.add(valorField);

        JButton continuarBtn = new JButton("Continuar");

        continuarBtn.addActionListener(e -> {
            try {
                double valor = Double.parseDouble(valorField.getText().replace(",", "."));
                controller.processarValorCompra(user, valor);
                dispose(); // fecha a janela depois de chamar o controller
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um valor válido (ex: 123.45)", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(label);
        panel.add(valorPanel);
        panel.add(continuarBtn);

        add(panel);
        SwingUtilities.invokeLater(valorField::requestFocusInWindow);
    }
}