package Frames;

import javax.swing.*;
import java.awt.*;
import transacaosegura.Estabelecimento;
import transacaosegura.SystemInfo;
import transacaosegura.Transacao;
import transacaosegura.Usuario;

public class ConfirmacaoFrame extends JFrame {
    private final Transacao transaction; // Vamos armazenar a transação aqui

    public ConfirmacaoFrame(Usuario user, double valorCompra) {
        String nomeEstabelecimento = SystemInfo.getNomeComputador();
        // Criamos uma instância de Estabelecimento. Em um sistema real, viria de um DB.
        Estabelecimento estabelecimento = new Estabelecimento(nomeEstabelecimento, "ID_MAQUINA_GERADO");

        // Criamos a transação com status PENDENTE inicialmente
        // O "id" da transação agora é gerado dentro da própria classe Transacao
        this.transaction = new Transacao(user.getId(), valorCompra, estabelecimento);

        String localizacao = "Localização aproximada não disponível"; // Placeholder por enquanto

        setTitle("Confirmação de Compra");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Painel principal com layout vertical
        JPanel mainPanel = new RoundedPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel celularLabel = new JLabel(user.getNumeroCelular(), SwingConstants.CENTER);
        celularLabel.setFont(new Font("Arial", Font.BOLD, 16));
        celularLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centraliza o texto no JLabel

        JLabel nomeLabel = new JLabel(user.getNome(), SwingConstants.CENTER);
        nomeLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        nomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valorLabel = new JLabel("Compra no valor de: R$ " + String.format("%.2f", valorCompra));
        JLabel lojaLabel = new JLabel("Na loja: " + nomeEstabelecimento);
        JLabel localizacaoLabel = new JLabel("Na localização: " + localizacao);

        valorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        lojaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        localizacaoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Centralizar os JLabels de informação
        valorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lojaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        localizacaoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        mainPanel.add(celularLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(nomeLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(valorLabel);
        mainPanel.add(lojaLabel);
        mainPanel.add(localizacaoLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(criarPainelBotoes());

        add(mainPanel);
    }

    private JPanel criarPainelBotoes() {
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JButton btnReconheco = new JButton("Reconheço");
        JButton btnNaoReconheco = new JButton("Não Reconheço");

        // Ação do botão "Reconheço"
        btnReconheco.addActionListener(e -> {
            transaction.setStatus(Transacao.Status.APROVADA);
            JOptionPane.showMessageDialog(this, "Compra realizada com sucesso!\nDetalhes: " + transaction, "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            System.exit(0);
        });

        // Ação do botão "Não Reconheço"
        btnNaoReconheco.addActionListener(e -> {
            transaction.setStatus(Transacao.Status.RECUSADA);
            JOptionPane.showMessageDialog(this, "Compra recusada!\nDetalhes: " + transaction, "Atenção!", JOptionPane.WARNING_MESSAGE);
            dispose();
            System.exit(0);
        });

        botoesPanel.add(btnReconheco);
        botoesPanel.add(btnNaoReconheco);
        botoesPanel.setOpaque(false); // Mantém o fundo transparente

        return botoesPanel;
    }
    // Painel com cantos arredondados (mantido como está)
    static class RoundedPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
            g2d.dispose();
        }
    }
}