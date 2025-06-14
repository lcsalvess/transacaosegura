package util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RazaoSocialFilter extends DocumentFilter {
    private final String regexPermitido = "[\\p{L}\\p{N} .,&-]+";

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string != null && string.matches(regexPermitido)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
        if (string != null && string.matches(regexPermitido)) {
            super.replace(fb, offset, length, string, attrs);
        }
    }
}
