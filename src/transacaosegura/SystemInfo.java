package transacaosegura;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class SystemInfo {

    public static String getNomeComputador() {
        try {
            return InetAddress.getLocalHost().getHostName(); // Obtém o nome do host da máquina
        } catch (UnknownHostException e) {
            logger.warning("Erro ao obter nome do host: " + e.getMessage());
            return "Nome do Computador Desconhecido"; // Retorno padrão em caso de erro
        }
    }

    private static final Logger logger = Logger.getLogger(SystemInfo.class.getName());

}