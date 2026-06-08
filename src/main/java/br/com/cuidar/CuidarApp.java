package br.com.cuidar;

import br.com.cuidar.model.Atividade;
import br.com.cuidar.model.Cargo;
import br.com.cuidar.model.Funcionario;
import br.com.cuidar.model.Medicamento;
import br.com.cuidar.model.Medico;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.model.Prontuario;
import br.com.cuidar.model.Quarto;
import br.com.cuidar.model.RegistroClinico;
import br.com.cuidar.model.Residente;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 1.3 — completa o conjunto de 12 modelos de domínio adicionando os
 * modelos clínicos (Prontuario, Medicamento, RegistroClinico, Atividade).
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 1.3 — Modelos clínicos\n");

        // Reaproveita uma pequena base das versões anteriores
        Cargo enfermeiro = new Cargo("Enfermeiro", "Cuidados de enfermagem");
        enfermeiro.setId(3);

        Pessoa pFunc = new Pessoa("Bruno Lima", "111.222.333-44", "Masculino",
                LocalDate.of(1988, 3, 12));
        pFunc.setId(11);
        Funcionario func = new Funcionario(pFunc, enfermeiro, "bruno.lima", "secreta",
                "Manhã", "(11) 95555-1111", "bruno@cuidar.com",
                "Rua A", 100, "01010-000");
        func.setId(6);

        Pessoa pMed = new Pessoa("Dra. Helena Costa", "555.666.777-88", "Feminino",
                LocalDate.of(1975, 11, 2));
        pMed.setId(12);
        Medico medico = new Medico(pMed, "CRM-SP 99887", "Geriatria",
                "(11) 94444-2222", "helena@cuidar.com");
        medico.setId(3);

        Quarto q102 = new Quarto(102, "Ocupado");
        q102.setId(2);

        Pessoa pResid = new Pessoa("Maria Aparecida", "777.888.999-00", "Feminino",
                LocalDate.of(1938, 6, 30));
        pResid.setId(22);
        Residente residente = new Residente(pResid, q102, "Ativo",
                "Hipertensa, em acompanhamento.");
        residente.setId(2);

        // === Modelos novos da v1.3 ===
        Prontuario prontuario = new Prontuario(residente, 62.5, 1.58,
                "O+", "Penicilina", "Acompanhamento mensal.");
        prontuario.setId(1);

        Medicamento dipirona = new Medicamento("Dipirona 500mg", "Medley",
                LocalDate.of(2027, 12, 31), 120, "Analgésico e antitérmico");
        dipirona.setId(1);

        RegistroClinico registro = new RegistroClinico(residente, func, dipirona, medico,
                "Administração de medicamento", null, LocalDate.now(), "1 comprimido");
        registro.setId(1);

        Atividade fisioterapia = new Atividade("Fisioterapia em grupo",
                "Sessão de alongamento e mobilidade",
                "Quarta", LocalTime.of(9, 0), LocalTime.of(10, 0));
        fisioterapia.setId(1);

        System.out.println(prontuario);
        System.out.println("Possui alergia? " + prontuario.possuiAlergia());
        System.out.println(dipirona + " (válido? " + dipirona.estaValido() + ")");
        System.out.println(registro);
        System.out.println(fisioterapia);
    }
}
