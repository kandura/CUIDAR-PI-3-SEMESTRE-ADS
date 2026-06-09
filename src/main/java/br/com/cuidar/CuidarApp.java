package br.com.cuidar;

import br.com.cuidar.controller.*;
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
 * Versão 3.2 — introduz a camada de controllers (7 fachadas finas) que orquestram
 * os 9 services. É o terceiro andar do MVC, pronto para receber a GUI Swing
 * a partir da v3.3.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 3.2 — Camada de controllers (7 controllers)\n");

        // Repositórios
        PessoaRepository pessoaRepo = new PessoaRepositoryImpl();
        FuncionarioRepository funcRepo = new FuncionarioRepositoryImpl();
        MedicoRepository medicoRepo = new MedicoRepositoryImpl();
        ResidenteRepository resRepo = new ResidenteRepositoryImpl();
        ResponsavelRepository respRepo = new ResponsavelRepositoryImpl();
        ResidenteResponsavelRepository rrRepo = new ResidenteResponsavelRepositoryImpl();
        ProntuarioRepository pronRepo = new ProntuarioRepositoryImpl();
        MedicamentoRepository medRepo = new MedicamentoRepositoryImpl();
        RegistroClinicoRepository rcRepo = new RegistroClinicoRepositoryImpl();
        AtividadeRepository atvRepo = new AtividadeRepositoryImpl();

        // Services
        ResidenteService residenteService = new ResidenteService(resRepo, pessoaRepo);
        FuncionarioService funcionarioService = new FuncionarioService(funcRepo, pessoaRepo);
        MedicoService medicoService = new MedicoService(medicoRepo, pessoaRepo);
        MedicamentoService medicamentoService = new MedicamentoService(medRepo);
        ProntuarioService prontuarioService = new ProntuarioService(pronRepo);
        RegistroClinicoService rcService = new RegistroClinicoService(rcRepo);
        ResponsavelService responsavelService = new ResponsavelService(respRepo, rrRepo, pessoaRepo);
        AtividadeService atividadeService = new AtividadeService(atvRepo);

        // Controllers
        ResidenteController residenteController = new ResidenteController(residenteService, responsavelService);
        FuncionarioController funcionarioController = new FuncionarioController(funcionarioService);
        MedicoController medicoController = new MedicoController(medicoService);
        MedicamentoController medicamentoController = new MedicamentoController(medicamentoService);
        ProntuarioController prontuarioController = new ProntuarioController(prontuarioService);
        RegistroClinicoController rcController = new RegistroClinicoController(rcService);
        AtividadeController atividadeController = new AtividadeController(atividadeService);

        System.out.println("Controllers instanciados: 7 ("
                + residenteController.getClass().getSimpleName() + ", "
                + funcionarioController.getClass().getSimpleName() + ", "
                + medicoController.getClass().getSimpleName() + ", "
                + medicamentoController.getClass().getSimpleName() + ", "
                + prontuarioController.getClass().getSimpleName() + ", "
                + rcController.getClass().getSimpleName() + ", "
                + atividadeController.getClass().getSimpleName() + ")\n");

        try {
            List<Residente> residentes = residenteController.listarTodos();
            System.out.println("[ResidenteController] listarTodos -> " + residentes.size());

            List<Funcionario> funcs = funcionarioController.listarTodos();
            System.out.println("[FuncionarioController] listarTodos -> " + funcs.size());

            List<Medico> medicos = medicoController.listarTodos();
            System.out.println("[MedicoController] listarTodos -> " + medicos.size());

            List<Medicamento> meds = medicamentoController.listarTodos();
            System.out.println("[MedicamentoController] listarTodos -> " + meds.size());

            List<Atividade> atvs = atividadeController.listarTodos();
            System.out.println("[AtividadeController] listarTodos -> " + atvs.size());

            if (!residentes.isEmpty()) {
                Residente r = residentes.get(0);
                System.out.println("\n--- Visão do residente '" + r.getPessoa().getNomeCompleto() + "' ---");
                System.out.println("[ResidenteController] buscarPorCpf('" + r.getPessoa().getCpf() + "') -> "
                        + (residenteController.buscarPorCpf(r.getPessoa().getCpf()) != null ? "encontrado" : "nulo"));
                System.out.println("[ResidenteController] listarResponsaveis -> "
                        + residenteController.listarResponsaveis(r).size() + " vínculo(s)");
                System.out.println("[ProntuarioController] buscarPorResidente -> "
                        + (prontuarioController.buscarPorResidente(r) != null ? "encontrado" : "nenhum"));
                System.out.println("[RCController] listarPorResidente -> "
                        + rcController.listarPorResidente(r).size() + " registro(s)");
            }

            System.out.println("[AtividadeController] listarPorDia('Sexta') -> "
                    + atividadeController.listarPorDia("Sexta").size());
        } catch (RuntimeException e) {
            System.err.println("Falha ao exercitar controllers: " + e.getMessage());
        }
    }
}
