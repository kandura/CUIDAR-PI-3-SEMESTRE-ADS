package br.com.cuidar.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Fábrica de conexões com o banco de dados PostgreSQL.
 * Centraliza a criação e o fechamento de conexões JDBC.
 * Prioridade: variáveis de ambiente > application.properties > valores padrão.
 */
public class ConnectionFactory {

    private static final String URL;
    private static final String USUARIO;
    private static final String SENHA;

    static {
        Properties props = new Properties();
        try (InputStream input = ConnectionFactory.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar application.properties: " + e.getMessage());
        }

        String host = getConfig("DB_HOST", "db.host", props, "localhost");
        String port = getConfig("DB_PORT", "db.port", props, "5432");
        String name = getConfig("DB_NAME", "db.name", props, "cuidar");

        URL = "jdbc:postgresql://" + host + ":" + port + "/" + name;
        USUARIO = getConfig("DB_USER", "db.user", props, "postgres");
        SENHA = getConfig("DB_PASSWORD", "db.password", props, "postgres");
    }

    /**
     * Busca configuração priorizando: variável de ambiente > properties > padrão.
     */
    private static String getConfig(String envKey, String propKey, Properties props, String defaultValue) {
        String env = System.getenv(envKey);
        if (env != null && !env.isEmpty()) {
            return env;
        }
        return props.getProperty(propKey, defaultValue);
    }

    /**
     * Cria e retorna uma nova conexão com o banco de dados.
     *
     * @return conexão JDBC ativa
     * @throws SQLException se não for possível conectar ao banco
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    /**
     * Fecha uma conexão de forma segura, tratando possíveis exceções.
     *
     * @param connection conexão a ser fechada
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
