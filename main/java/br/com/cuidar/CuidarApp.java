package br.com.cuidar;

import br.com.cuidar.model.Cargo;
import br.com.cuidar.model.Funcionario;
import br.com.cuidar.model.Medico;
import br.com.cuidar.model.Pessoa;

import java.time.LocalDate;

/**
 * Classe principal do sistema CUIDAR.
 * Nesta versão (1.1) o app apenas instancia os modelos básicos
 * (Pessoa, Cargo, Funcionario, Medico) e imprime no console para
 * demonstrar que as classes de domínio compilam e funcionam.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 1.1 — Protótipo de modelos básicos\n");

        Cargo admin = new Cargo("Administrador", "Acesso total ao sistema");
        admin.setId(1);

        Pessoa p1 = new Pessoa("Maria Silva", "111.222.333-44", "Feminino",
                LocalDate.of(1985, 5, 20));
        p1.setId(1);

        Funcionario f1 = new Funcionario(p1, admin, "maria.silva", "123456",
                "Manhã", "(11) 99999-0000", "maria@cuidar.com",
                "Rua das Flores", 100, "01010-000");
        f1.setId(1);

        Pessoa p2 = new Pessoa("Dr. João Souza", "555.666.777-88", "Masculino",
                LocalDate.of(1970, 3, 10));
        p2.setId(2);

        Medico m1 = new Medico(p2, "CRM-12345", "Geriatria",
                "(11) 98888-1111", "joao@medico.com");
        m1.setId(1);

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(admin);
        System.out.println(f1);
        System.out.println(m1);
    }
}
