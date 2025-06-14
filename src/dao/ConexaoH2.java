package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoH2 {
    private static final String URL = "jdbc:h2:~/PSC"; // URL do banco de dados
    private static final String USER = "sa"; // Usuário do banco de dados
    private static final String PASSWORD = ""; // Senha do banco de dados

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
