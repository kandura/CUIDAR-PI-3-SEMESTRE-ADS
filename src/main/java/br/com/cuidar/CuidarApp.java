package br.com.cuidar;

import br.com.cuidar.model.Cargo;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.model.Quarto;
import br.com.cuidar.repository.CargoRepository;
import br.com.cuidar.repository.PessoaRepository;
import br.com.cuidar.repository.QuartoRepository;
import br.com.cuidar.repository.impl.CargoRepositoryImpl;
import br.com.cuidar.repository.impl.PessoaRepositoryImpl;
import br.com.cuidar.repository.impl.QuartoRepositoryImpl;

import java.util.List;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 2.3 — primeiras implementações JDBC dos repositórios:
 * {@code PessoaRepositoryImpl}, {@code CargoRepositoryImpl}, {@code QuartoRepositoryImpl}.
 *
 * Demonstra apenas leituras (listar) para não alterar o banco do grupo.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 2.3 — Repositórios JDBC: Pessoa, Cargo, Quarto\n");

        PessoaRepository pessoaRepo = new PessoaRepositoryImpl();
        CargoRepository cargoRepo = new CargoRepositoryImpl();
        QuartoRepository quartoRepo = new QuartoRepositoryImpl();

        try {
            List<Cargo> cargos = cargoRepo.listarTodos();
            System.out.println("Cargos cadastrados (" + cargos.size() + "):");
            for (Cargo c : cargos) {
                System.out.println("  - " + c.getId() + " | " + c.getNomeCargo());
            }

            List<Quarto> quartos = quartoRepo.listarTodos();
            System.out.println("\nQuartos cadastrados (" + quartos.size() + "):");
            for (Quarto q : quartos) {
                System.out.println("  - " + q);
            }

            List<Pessoa> pessoas = pessoaRepo.listarTodos();
            System.out.println("\nPessoas cadastradas (" + pessoas.size() + "):");
            int max = Math.min(pessoas.size(), 5);
            for (int i = 0; i < max; i++) {
                Pessoa p = pessoas.get(i);
                System.out.println("  - " + p.getId() + " | " + p.getNomeCompleto()
                        + " | CPF " + p.getCpf());
            }
            if (pessoas.size() > max) {
                System.out.println("  ... (" + (pessoas.size() - max) + " restantes)");
            }
        } catch (RuntimeException e) {
            System.err.println("Falha ao consultar o banco: " + e.getMessage());
            System.err.println("Verifique se o PostgreSQL está rodando e se "
                    + "src/main/resources/application.properties está configurado.");
        }
    }
}
