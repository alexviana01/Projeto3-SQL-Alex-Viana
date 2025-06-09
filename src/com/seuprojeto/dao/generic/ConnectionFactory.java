package com.seuprojeto.dao.generic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection connection;

    private ConnectionFactory() {
        // Construtor privado para Singleton
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = initConnection();
        }
        return connection;
    }

    private static Connection initConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            // --- CONFIGURE COM AS CREDENCIAIS DO SEU SERVIDOR POSTGRESQL ---
            // servidorHost deve ser apenas o hostname/IP
            String servidorHost = "localhost";
            // Verifique se a porta 5431 está correta para o seu PostgreSQL.
            // Se não tiver certeza, tente 5432 (porta padrão).
            String servidorPorta = "5432"; // OU "5432"

            // CORRIGIDO: nomeBanco deve ser APENAS o nome do banco de dados.
            // Use o nome EXATO que você verificou no pgAdmin.
            String nomeBanco = "modulo30SQLEbac"; // <--- MUITO PROVAVELMENTE ESTE É O NOME CORRETO

            // O usuário e a senha são passados separadamente para DriverManager.getConnection()
            String usuario = "postgres"; // Seu nome de usuário do PostgreSQL
            String senha = "2025al";     // Sua senha do PostgreSQL
            // --- FIM DA CONFIGURAÇÃO ---

            // A construção da URL JDBC agora está correta
            String jdbcUrl = "jdbc:postgresql://" + servidorHost + ":" + servidorPorta + "/" + nomeBanco;

            System.out.println("Tentando conectar ao banco de dados: " + jdbcUrl);
            // O nome de usuário e a senha são passados aqui, não na URL
            Connection conn = DriverManager.getConnection(jdbcUrl, usuario, senha);
            System.out.println("Conexão com o PostgreSQL estabelecida com sucesso!");
            return conn;

        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do PostgreSQL não encontrado! Verifique o classpath.");
            throw new RuntimeException("Erro ao carregar o driver JDBC: " + e.getMessage(), e);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados PostgreSQL:");
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("ErrorCode: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Conexão com o PostgreSQL fechada.");
                }
                connection = null;
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o PostgreSQL: " + e.getMessage());
            }
        }
    }
}