package Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import transacaosegura.Estabelecimento;
import transacaosegura.Transacao;
import transacaosegura.Usuario;

public class ValorCompraFrame extends JFrame {
    public ValorCompraFrame(Usuario user) {
        setTitle("Valor da Compra");
        setSize(400, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Digite o valor da compra:");
        JTextField valorField = new JTextField(10);
        JButton continuarBtn = new JButton("Continuar");

        continuarBtn.addActionListener(e -> {
            try {
                double valor = Double.parseDouble(valorField.getText().replace(",", "."));

                // --- INÍCIO DA LÓGICA REVISADA ---

                // Primeiro, vamos criar o Estabelecimento (que pode ser mockado aqui)
                String nomeEstabelecimento;
                try {
                    nomeEstabelecimento = InetAddress.getLocalHost().getHostName();
                } catch (UnknownHostException exHost) {
                    nomeEstabelecimento = "Nome do Computador Desconhecido";
                    System.err.println("Erro ao obter nome do host: " + exHost.getMessage());
                }
                Estabelecimento estabelecimento = new Estabelecimento(nomeEstabelecimento, "ID_MAQUINA_FICTICIO");

                // Agora, criamos a Transação
                Transacao novaTransacao = new Transacao(user.getId(), valor, estabelecimento);

                // Verificamos a regra de negócio na própria Transação
                if (novaTransacao.isConfirmacaoNecessaria()) {
                    // Se a confirmação for necessária (valor > 50), abre a tela de confirmação
                    dispose();
                    new ConfirmacaoFrame(user, valor).setVisible(true);
                } else {
                    // Se a confirmação NÃO for necessária (valor <= 50), aprova automaticamente
                    // E se quiser, já pode definir o status da transação para APROVADA aqui
                    novaTransacao.setStatus(Transacao.Status.APROVADA);

                    JOptionPane.showMessageDialog(this,
                            "Compra no valor de R$ " + String.format("%.2f", valor) +
                            " aprovada automaticamente. Não requer confirmação.",
                            "Compra Aprovada",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    System.exit(0); // Fecha o aplicativo
                }
                // --- FIM DA LÓGICA REVISADA ---

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite um valor válido (ex: 123.45)", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(label);
        panel.add(valorField);
        panel.add(continuarBtn);

        add(panel);
    }
}