import frames.CadastroFrame;
import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        // Garantir que a interface Swing rode na thread correta
        SwingUtilities.invokeLater(() -> {
            CadastroFrame cadastroFrame = new CadastroFrame();
            cadastroFrame.setVisible(true);
        });
    }
}