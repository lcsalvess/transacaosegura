package dao;

import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoH2 {
    private static Connection conexao;

    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            conexao = DriverManager.getConnection("jdbc:h2:~/meubanco", "sa", "");
            criarTabelas(); // Cria tabelas se não existirem
        }
        return conexao;
    }

    private static void criarTabelas() throws SQLException {
        Statement stmt = conexao.createStatement();

        // Tabela usuario
        stmt.execute("""
                    CREATE TABLE IF NOT EXISTS usuario (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(255) NOT NULL,
                        numero_celular VARCHAR(20),
                        ativo BOOLEAN DEFAULT TRUE
                    );
                """);

        // Tabela pessoa_fisica
        stmt.execute("""
                    CREATE TABLE IF NOT EXISTS pessoa_fisica (
                        id_usuario INT PRIMARY KEY,
                        cpf VARCHAR(14) NOT NULL,
                        FOREIGN KEY (id_usuario) REFERENCES usuario(id)
                    );
                """);

        // Tabela pessoa_juridica
        stmt.execute("""
                    CREATE TABLE IF NOT EXISTS pessoa_juridica (
                        id_usuario INT PRIMARY KEY,
                        cnpj VARCHAR(18) NOT NULL,
                        razao_social VARCHAR(255) NOT NULL,
                        FOREIGN KEY (id_usuario) REFERENCES usuario(id)
                    );
                """);

        // Tabela estabelecimento
        stmt.execute("""
                    CREATE TABLE IF NOT EXISTS estabelecimento (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(255) NOT NULL
                    );
                """);

        // Tabela transacao
        stmt.execute("""
                    CREATE TABLE IF NOT EXISTS transacao (
                        id VARCHAR(36) PRIMARY KEY,
                        id_usuario INT NOT NULL,
                        valor DOUBLE NOT NULL,
                        status VARCHAR(20) NOT NULL,
                        data_hora TIMESTAMP NOT NULL,
                        id_estabelecimento INT NOT NULL,
                        FOREIGN KEY (id_usuario) REFERENCES usuario(id),
                        FOREIGN KEY (id_estabelecimento) REFERENCES estabelecimento(id)
                    );
                """);
    }
}