package br.com.cuidar;

import br.com.cuidar.controller.*;
import br.com.cuidar.model.Funcionario;
import br.com.cuidar.repository.*;
import br.com.cuidar.repository.impl.*;
import br.com.cuidar.service.*;
import br.com.cuidar.view.LoginFrame;
import br.com.cuidar.view.MainFrame;

import javax.swing.*;
import java.awt.GraphicsEnvironment;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 3.5 — Todas as 5 abas têm painel real: {@code CadastroResidentePanel},
 * {@code ControleMedicamentoPanel}, {@code GestaoAtividadePanel},
 * {@code ProntuarioPanel} e {@code ControleAdministrativoPanel}. Não há mais
 * placeholders. A v3.6 adicionará RBAC dinâmico na sidebar (alguns itens
 * sumirem por cargo), o fluxo de "Trocar de conta" e o PBKDF2 + migração de
 * senhas legadas no {@code LoginService}.
 *
 * <p>Sem argumentos: tenta abrir a GUI. Em ambiente sem display gráfico,
 * faz fallback para um smoke-test "headless" que exercita os 7 controllers
 * usados pelos painéis novos.</p>
 */
public class CuidarApp {

    public static void main(String[] args) {
        boolean forceHeadless = args.length > 0 && "--headless".equalsIgnoreCase(args[0]);
        boolean headless = forceHeadless || GraphicsEnvironment.isHeadless();

        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 3.5 — Medicamento + Atividade + Prontuário panels (5/5 telas)");

        // Repositórios
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

        // Services
        LoginService loginService = new LoginService(funcRepo);
        ResidenteService residenteService = new ResidenteService(resRepo, pessoaRepo);
        FuncionarioService funcionarioService = new FuncionarioService(funcRepo, pessoaRepo);
        MedicoService medicoService = new MedicoService(medicoRepo, pessoaRepo);
        ResponsavelService responsavelService = new ResponsavelService(respRepo, rrRepo, pessoaRepo);
        ProntuarioService prontuarioService = new ProntuarioService(pronRepo);
        MedicamentoService medicamentoService = new MedicamentoService(medRepo);
        RegistroClinicoService registroClinicoService = new RegistroClinicoService(rcRepo);
        AtividadeService atividadeService = new AtividadeService(atvRepo);

        // Controllers
        ResidenteController residenteController = new ResidenteController(residenteService, responsavelService);
        FuncionarioController funcionarioController = new FuncionarioController(funcionarioService);
        MedicoController medicoController = new MedicoController(medicoService);
        MedicamentoController medicamentoController = new MedicamentoController(medicamentoService);
        AtividadeController atividadeController = new AtividadeController(atividadeService);
        ProntuarioController prontuarioController = new ProntuarioController(prontuarioService);
        RegistroClinicoController registroClinicoController = new RegistroClinicoController(registroClinicoService);

        if (headless) {
            System.out.println("[modo headless] sem display — exercitando controllers das 5 telas.\n");
            try {
                System.out.println("[ResidenteController]      listarTodos -> "
                        + residenteController.listarTodos().size());
                System.out.println("[FuncionarioController]    listarTodos -> "
                        + funcionarioController.listarTodos().size());
                System.out.println("[MedicoController]         listarTodos -> "
                        + medicoController.listarTodos().size());
                System.out.println("[MedicamentoController]    listarTodos -> "
                        + medicamentoController.listarTodos().size());
                System.out.println("[AtividadeController]      listarTodos -> "
                        + atividadeController.listarTodos().size());
                System.out.println("[QuartoRepository]         listarTodos -> "
                        + quartoRepo.listarTodos().size());
                System.out.println("[CargoRepository]          listarTodos -> "
                        + cargoRepo.listarTodos().size());

                // Prontuário + registros clínicos: usa um residente cadastrado
                var residentes = residenteController.listarTodos();
                if (!residentes.isEmpty()) {
                    var r0 = residentes.get(0);
                    var p = prontuarioController.buscarPorResidente(r0);
                    System.out.println("\n[ProntuarioController] residente '"
                            + r0.getPessoa().getNomeCompleto() + "' -> "
                            + (p != null ? "prontuário OK (peso=" + p.getPeso() + ", altura=" + p.getAltura() + ")"
                                          : "sem prontuário"));
                    System.out.println("[RegistroClinicoController] registros do mesmo residente -> "
                            + registroClinicoController.listarPorResidente(r0).size());
                }
                System.out.println("\n[modo headless] OK.");
            } catch (RuntimeException e) {
                System.err.println("Falha headless: " + e.getMessage());
            }
            return;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(loginService);
            login.setVisible(true);
            login.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    Funcionario f = login.getFuncionarioLogado();
                    if (f != null) {
                        new MainFrame(f, residenteController, funcionarioController,
                                medicoController, medicamentoController, atividadeController,
                                prontuarioController, registroClinicoController,
                                quartoRepo, cargoRepo).setVisible(true);
                    } else {
                        System.exit(0);
                    }
                }
            });
        });
    }
}
