package util;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatadorNumerico {
    public static NumberFormatter criarNumberFormatter() {
        Locale locale = Locale.forLanguageTag("pt-BR");
        // Define os símbolos de formatação para o Brasil
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

        // Define padrão com duas casas decimais
        DecimalFormat format = new DecimalFormat("0.00", symbols);
        format.setGroupingUsed(false); // Não usar separador de milhar (ex: 1.000)

        // Cria formatter para JTextField
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setAllowsInvalid(false); // Bloqueia letras e símbolos inválidos
        formatter.setMinimum(0.0);
        formatter.setMaximum(Double.MAX_VALUE);

        return formatter;
    }
}
