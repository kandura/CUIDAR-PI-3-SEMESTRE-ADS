package br.com.cuidar;

import br.com.cuidar.config.ConnectionFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 2.1 — adiciona a camada de configuração de banco de dados
 * (ConnectionFactory + application.properties + schema SQL).
 * O main tenta abrir uma conexão e exibe o resultado.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 2.1 — Configuração de banco\n");

        System.out.println("Tentando conectar ao PostgreSQL...");
        try (Connection conn = ConnectionFactory.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("Conexão OK!");
            System.out.println("  Banco:   " + meta.getDatabaseProductName()
                    + " " + meta.getDatabaseProductVersion());
            System.out.println("  Driver:  " + meta.getDriverName()
                    + " " + meta.getDriverVersion());
            System.out.println("  URL:     " + meta.getURL());
            System.out.println("  Usuário: " + meta.getUserName());
        } catch (SQLException e) {
            System.err.println("Falha ao conectar: " + e.getMessage());
            System.err.println("Verifique se o PostgreSQL está rodando e se "
                    + "src/main/resources/application.properties está configurado "
                    + "(copie application.properties.example).");
        }
    }
}
