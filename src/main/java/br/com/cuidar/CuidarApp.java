package br.com.cuidar;

import br.com.cuidar.model.Funcionario;
import br.com.cuidar.model.Medico;
import br.com.cuidar.repository.FuncionarioRepository;
import br.com.cuidar.repository.MedicoRepository;
import br.com.cuidar.repository.impl.FuncionarioRepositoryImpl;
import br.com.cuidar.repository.impl.MedicoRepositoryImpl;

import java.util.List;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 2.4 — adiciona as implementações JDBC de {@code FuncionarioRepositoryImpl}
 * e {@code MedicoRepositoryImpl}, com JOINs em {@code pessoa} (e {@code cargo} para
 * funcionário) para devolver o objeto de domínio já montado.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 2.4 — Repositórios JDBC: Funcionario, Medico\n");

        FuncionarioRepository funcRepo = new FuncionarioRepositoryImpl();
        MedicoRepository medRepo = new MedicoRepositoryImpl();

        try {
            List<Funcionario> funcionarios = funcRepo.listarTodos();
            System.out.println("Funcionários cadastrados (" + funcionarios.size() + "):");
            for (Funcionario f : funcionarios) {
                System.out.println("  - " + f.getId() + " | " + f.getPessoa().getNomeCompleto()
                        + " | login=" + f.getLogin()
                        + " | cargo=" + f.getCargo().getNomeCargo()
                        + " | turno=" + f.getTurno());
            }

            List<Medico> medicos = medRepo.listarTodos();
            System.out.println("\nMédicos cadastrados (" + medicos.size() + "):");
            for (Medico m : medicos) {
                System.out.println("  - " + m.getId() + " | " + m.getPessoa().getNomeCompleto()
                        + " | CRM " + m.getCrm()
                        + " | " + m.getEspecialidade());
            }
        } catch (RuntimeException e) {
            System.err.println("Falha ao consultar o banco: " + e.getMessage());
            System.err.println("Verifique se o PostgreSQL está rodando e se "
                    + "src/main/resources/application.properties está configurado.");
        }
    }
}
