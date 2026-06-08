package br.com.cuidar;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.repository.AtividadeRepository;
import br.com.cuidar.repository.CargoRepository;
import br.com.cuidar.repository.FuncionarioRepository;
import br.com.cuidar.repository.MedicamentoRepository;
import br.com.cuidar.repository.MedicoRepository;
import br.com.cuidar.repository.PessoaRepository;
import br.com.cuidar.repository.ProntuarioRepository;
import br.com.cuidar.repository.QuartoRepository;
import br.com.cuidar.repository.RegistroClinicoRepository;
import br.com.cuidar.repository.ResidenteRepository;
import br.com.cuidar.repository.ResidenteResponsavelRepository;
import br.com.cuidar.repository.ResponsavelRepository;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 2.2 — define o contrato de persistência: 12 interfaces de Repository
 * (uma para cada entidade do domínio). Ainda não há implementação JDBC.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 2.2 — Interfaces de Repository\n");

        Class<?>[] repos = {
                PessoaRepository.class,
                CargoRepository.class,
                FuncionarioRepository.class,
                MedicoRepository.class,
                QuartoRepository.class,
                ResidenteRepository.class,
                ResponsavelRepository.class,
                ResidenteResponsavelRepository.class,
                ProntuarioRepository.class,
                MedicamentoRepository.class,
                RegistroClinicoRepository.class,
                AtividadeRepository.class
        };

        System.out.println("Interfaces de Repository definidas: " + repos.length);
        for (Class<?> repo : repos) {
            System.out.println("  - " + repo.getSimpleName()
                    + " (" + repo.getDeclaredMethods().length + " métodos)");
        }

        System.out.println("\nTestando conexão com PostgreSQL...");
        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("Conexão OK: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            System.err.println("Falha ao conectar: " + e.getMessage());
        }
    }
}
