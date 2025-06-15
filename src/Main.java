import dao.ConexaoH2;
import frames.MenuPrincipalFrame;
import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection conexao = ConexaoH2.getConexao();
                MenuPrincipalFrame menu = new MenuPrincipalFrame(conexao);
                menu.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco: " + e.getMessage());
            }
        });
    }
}