package br.com.cuidar;

import br.com.cuidar.controller.*;
import br.com.cuidar.model.Funcionario;
import br.com.cuidar.repository.*;
import br.com.cuidar.repository.impl.*;
import br.com.cuidar.service.*;
import br.com.cuidar.view.LoginFrame;
import br.com.cuidar.view.MainFrame;

import javax.swing.*;

/**
 * Classe principal do sistema CUIDAR.
 * Ponto de entrada da aplicação desktop.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Inicializando...");

        // Repositories
        PessoaRepository pessoaRepo = new PessoaRepositoryImpl();
        QuartoRepository quartoRepo = new QuartoRepositoryImpl();
        CargoRepository cargoRepo = new CargoRepositoryImpl();
        ResidenteRepository residenteRepo = new ResidenteRepositoryImpl();
        FuncionarioRepository funcionarioRepo = new FuncionarioRepositoryImpl();
        ResponsavelRepository responsavelRepo = new ResponsavelRepositoryImpl();
        MedicoRepository medicoRepo = new MedicoRepositoryImpl();
        ResidenteResponsavelRepository resResponsavelRepo = new ResidenteResponsavelRepositoryImpl();
        ProntuarioRepository prontuarioRepo = new ProntuarioRepositoryImpl();
        MedicamentoRepository medicamentoRepo = new MedicamentoRepositoryImpl();
        RegistroClinicoRepository registroClinicoRepo = new RegistroClinicoRepositoryImpl();
        AtividadeRepository atividadeRepo = new AtividadeRepositoryImpl();

        // Services
        LoginService loginService = new LoginService(funcionarioRepo);
        ResidenteService residenteService = new ResidenteService(residenteRepo, pessoaRepo);
        FuncionarioService funcionarioService = new FuncionarioService(funcionarioRepo, pessoaRepo);
        MedicoService medicoService = new MedicoService(medicoRepo, pessoaRepo);
        MedicamentoService medicamentoService = new MedicamentoService(medicamentoRepo);
        ProntuarioService prontuarioService = new ProntuarioService(prontuarioRepo);
        RegistroClinicoService registroClinicoService = new RegistroClinicoService(registroClinicoRepo);
        ResponsavelService responsavelService = new ResponsavelService(responsavelRepo, resResponsavelRepo, pessoaRepo);
        AtividadeService atividadeService = new AtividadeService(atividadeRepo);

        // Controllers
        ResidenteController residenteController = new ResidenteController(residenteService, responsavelService);
        FuncionarioController funcionarioController = new FuncionarioController(funcionarioService);
        MedicoController medicoController = new MedicoController(medicoService);
        MedicamentoController medicamentoController = new MedicamentoController(medicamentoService);
        ProntuarioController prontuarioController = new ProntuarioController(prontuarioService);
        RegistroClinicoController registroClinicoController = new RegistroClinicoController(registroClinicoService);
        AtividadeController atividadeController = new AtividadeController(atividadeService);

        // Swing UI
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // fallback para look and feel padrão
            }

            mostrarLogin(loginService, residenteController, funcionarioController, medicoController,
                    medicamentoController, prontuarioController, registroClinicoController,
                    atividadeController, quartoRepo, cargoRepo);
        });
    }

    private static void mostrarLogin(LoginService loginService,
                                     ResidenteController residenteController,
                                     FuncionarioController funcionarioController,
                                     MedicoController medicoController,
                                     MedicamentoController medicamentoController,
                                     ProntuarioController prontuarioController,
                                     RegistroClinicoController registroClinicoController,
                                     AtividadeController atividadeController,
                                     QuartoRepository quartoRepo,
                                     CargoRepository cargoRepo) {
        LoginFrame loginFrame = new LoginFrame(loginService);
        loginFrame.setVisible(true);

        new Thread(() -> {
            while (loginFrame.isVisible()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            Funcionario funcionarioLogado = loginFrame.getFuncionarioLogado();
            if (funcionarioLogado != null) {
                SwingUtilities.invokeLater(() -> {
                    Runnable onLogout = () -> mostrarLogin(loginService, residenteController,
                            funcionarioController, medicoController, medicamentoController,
                            prontuarioController, registroClinicoController, atividadeController,
                            quartoRepo, cargoRepo);
                    MainFrame mainFrame = new MainFrame(
                            funcionarioLogado,
                            residenteController,
                            funcionarioController,
                            medicoController,
                            medicamentoController,
                            prontuarioController,
                            registroClinicoController,
                            atividadeController,
                            quartoRepo,
                            cargoRepo,
                            onLogout
                    );
                    mainFrame.setVisible(true);
                });
            } else {
                System.exit(0);
            }
        }).start();
    }
}
