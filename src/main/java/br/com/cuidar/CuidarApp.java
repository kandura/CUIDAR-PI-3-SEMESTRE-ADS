package br.com.cuidar;

import br.com.cuidar.model.Atividade;
import br.com.cuidar.model.Funcionario;
import br.com.cuidar.model.Medicamento;
import br.com.cuidar.model.Medico;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.*;
import br.com.cuidar.repository.impl.*;
import br.com.cuidar.service.*;

import java.util.List;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 3.1 — introduz a camada de serviço (9 services) sobre os 12 repositórios.
 * Os services concentram a lógica de negócio (validação de CPF único, fluxo
 * pessoa+entidade no cadastro etc.). Ainda sem GUI: a {@code main} apenas
 * exercita os services consultando o banco real.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 3.1 — Camada de serviço (9 services)\n");

        PessoaRepository pessoaRepo = new PessoaRepositoryImpl();
        CargoRepository cargoRepo = new CargoRepositoryImpl();
        QuartoRepository quartoRepo = new QuartoRepositoryImpl();
        FuncionarioRepository funcRepo = new FuncionarioRepositoryImpl();
        MedicoRepository medicoRepo = new MedicoRepositoryImpl();
        ResidenteRepository resRepo = new ResidenteRepositoryImpl();
        ResponsavelRepository respRepo = new ResponsavelRepositoryImpl();
        ResidenteResponsavelRepository rrRepo = new ResidenteResponsavelRepositoryImpl();
        ProntuarioRepository pronRepo = new ProntuarioRepositoryImpl();
        MedicamentoRepository medRepo = new MedicamentoRepositoryImpl();
        RegistroClinicoRepository rcRepo = new RegistroClinicoRepositoryImpl();
        AtividadeRepository atvRepo = new AtividadeRepositoryImpl();

        LoginService loginService = new LoginService(funcRepo);
        ResidenteService residenteService = new ResidenteService(resRepo, pessoaRepo);
        FuncionarioService funcionarioService = new FuncionarioService(funcRepo, pessoaRepo);
        MedicoService medicoService = new MedicoService(medicoRepo, pessoaRepo);
        MedicamentoService medicamentoService = new MedicamentoService(medRepo);
        ProntuarioService prontuarioService = new ProntuarioService(pronRepo);
        RegistroClinicoService rcService = new RegistroClinicoService(rcRepo);
        ResponsavelService responsavelService = new ResponsavelService(respRepo, rrRepo, pessoaRepo);
        AtividadeService atividadeService = new AtividadeService(atvRepo);

        // evita "variável não utilizada" e prova que tudo cabe no main em conjunto.
        System.out.println("Services instanciados: " + 9 + " ("
                + loginService.getClass().getSimpleName() + ", "
                + residenteService.getClass().getSimpleName() + ", "
                + funcionarioService.getClass().getSimpleName() + ", "
                + medicoService.getClass().getSimpleName() + ", "
                + medicamentoService.getClass().getSimpleName() + ", "
                + prontuarioService.getClass().getSimpleName() + ", "
                + rcService.getClass().getSimpleName() + ", "
                + responsavelService.getClass().getSimpleName() + ", "
                + atividadeService.getClass().getSimpleName() + ")");
        System.out.println("Repositórios em uso: 12 (pessoa, cargo, quarto, funcionario, medico, "
                + "residente, responsavel, residente_responsavel, prontuario, medicamento, "
                + "registro_clinico, atividade) — refs: "
                + pessoaRepo.getClass().getSimpleName() + "/"
                + cargoRepo.getClass().getSimpleName() + "/"
                + quartoRepo.getClass().getSimpleName());

        try {
            List<Residente> residentes = residenteService.listarTodos();
            System.out.println("\n[ResidenteService] listarTodos -> " + residentes.size() + " residente(s)");

            List<Funcionario> funcs = funcionarioService.listarTodos();
            System.out.println("[FuncionarioService] listarTodos -> " + funcs.size() + " funcionário(s)");

            List<Medico> medicos = medicoService.listarTodos();
            System.out.println("[MedicoService] listarTodos -> " + medicos.size() + " médico(s)");

            List<Medicamento> medicamentos = medicamentoService.listarTodos();
            System.out.println("[MedicamentoService] listarTodos -> " + medicamentos.size() + " medicamento(s)");

            List<Atividade> atividades = atividadeService.listarTodos();
            System.out.println("[AtividadeService] listarTodos -> " + atividades.size() + " atividade(s)");

            if (!funcs.isEmpty()) {
                Funcionario primeiro = funcs.get(0);
                Funcionario auth = loginService.autenticar(primeiro.getLogin(), primeiro.getSenha());
                System.out.println("[LoginService] autenticar('" + primeiro.getLogin()
                        + "', '<senha-cadastrada>') -> " + (auth != null ? "OK ("
                        + auth.getPessoa().getNomeCompleto() + ")" : "FALHA"));
                Funcionario authBad = loginService.autenticar(primeiro.getLogin(), "senha_errada_xyz");
                System.out.println("[LoginService] autenticar(login, 'senha_errada_xyz') -> "
                        + (authBad != null ? "OK" : "negado (esperado)"));
            }

            if (!residentes.isEmpty()) {
                Residente r = residentes.get(0);
                System.out.println("[ProntuarioService] buscarPorResidente('"
                        + r.getPessoa().getNomeCompleto() + "') -> "
                        + (prontuarioService.buscarPorResidente(r) != null ? "encontrado" : "nenhum"));
                System.out.println("[RCService] listarPorResidente('"
                        + r.getPessoa().getNomeCompleto() + "') -> "
                        + rcService.listarPorResidente(r).size() + " registro(s)");
                System.out.println("[ResponsavelService] listarPorResidente('"
                        + r.getPessoa().getNomeCompleto() + "') -> "
                        + responsavelService.listarPorResidente(r).size() + " vínculo(s)");
            }
        } catch (RuntimeException e) {
            System.err.println("Falha ao exercitar services: " + e.getMessage());
        }
    }
}
