package transacaosegura;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemInfo {

    public static String getNomeComputador() {
        try {
            return InetAddress.getLocalHost().getHostName(); // Obtém o nome do host da máquina
        } catch (UnknownHostException e) {
            System.err.println("Erro ao obter nome do host: " + e.getMessage());
            return "Nome do Computador Desconhecido"; // Retorno padrão em caso de erro
        }
    }
}