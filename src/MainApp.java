import dao.ConexaoH2;
import frames.CadastroFrame;

import javax.swing.*;
import java.sql.Connection;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection conexao = ConexaoH2.getConexao();
                CadastroFrame frame = new CadastroFrame(conexao);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco: " + e.getMessage());
            }
        });
    }
}