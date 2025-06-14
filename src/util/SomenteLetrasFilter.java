package util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class SomenteLetrasFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String texto, AttributeSet attr) throws BadLocationException {
        if (apenasLetras(texto)) {
            super.insertString(fb, offset, texto, attr);
        }
    }
    @Override
    public void replace(FilterBypass fb, int offset, int length, String texto, AttributeSet attr) throws BadLocationException {
        if (apenasLetras(texto)) {
            super.replace(fb, offset, length, texto, attr);
        }
    }
    private boolean apenasLetras(String texto) {
        return texto != null && texto.matches("[a-zA-ZáéíóúãõâêîôûçÁÉÍÓÚÃÕÂÊÎÔÛÇ ]+");
    }
}
