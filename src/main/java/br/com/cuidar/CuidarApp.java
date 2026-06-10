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
 * Versão 3.4 — Plugin de painéis: a aba "Residentes" recebe
 * {@code CadastroResidentePanel} (formulário + tabela + filtro) e a aba
 * "Administrativo" recebe {@code ControleAdministrativoPanel} (abas
 * Funcionários/Médicos/Quartos para admin, "Meu Perfil" para os demais).
 * As outras três abas permanecem placeholder até a v3.5.
 *
 * <p>Sem argumentos: tenta abrir a GUI. Em ambiente sem display gráfico,
 * faz fallback para um smoke-test "headless" que apenas exercita os
 * controllers usados pelos panels novos.</p>
 */
public class CuidarApp {

    public static void main(String[] args) {
        boolean forceHeadless = args.length > 0 && "--headless".equalsIgnoreCase(args[0]);
        boolean headless = forceHeadless || GraphicsEnvironment.isHeadless();

        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 3.4 — CadastroResidentePanel + ControleAdministrativoPanel");

        // Repositórios
        PessoaRepository pessoaRepo = new PessoaRepositoryImpl();
        CargoRepository cargoRepo = new CargoRepositoryImpl();
        QuartoRepository quartoRepo = new QuartoRepositoryImpl();
        FuncionarioRepository funcRepo = new FuncionarioRepositoryImpl();
        MedicoRepository medicoRepo = new MedicoRepositoryImpl();
        ResidenteRepository resRepo = new ResidenteRepositoryImpl();
        ResponsavelRepository respRepo = new ResponsavelRepositoryImpl();
        ResidenteResponsavelRepository rrRepo = new ResidenteResponsavelRepositoryImpl();

        // Services
        LoginService loginService = new LoginService(funcRepo);
        ResidenteService residenteService = new ResidenteService(resRepo, pessoaRepo);
        FuncionarioService funcionarioService = new FuncionarioService(funcRepo, pessoaRepo);
        MedicoService medicoService = new MedicoService(medicoRepo, pessoaRepo);
        ResponsavelService responsavelService = new ResponsavelService(respRepo, rrRepo, pessoaRepo);

        // Controllers
        ResidenteController residenteController = new ResidenteController(residenteService, responsavelService);
        FuncionarioController funcionarioController = new FuncionarioController(funcionarioService);
        MedicoController medicoController = new MedicoController(medicoService);

        if (headless) {
            System.out.println("[modo headless] sem display — exercitando controllers usados pelos panels.\n");
            try {
                System.out.println("[ResidenteController] listarTodos -> "
                        + residenteController.listarTodos().size());
                System.out.println("[FuncionarioController] listarTodos -> "
                        + funcionarioController.listarTodos().size());
                System.out.println("[MedicoController] listarTodos -> "
                        + medicoController.listarTodos().size());
                System.out.println("[QuartoRepository] listarTodos -> "
                        + quartoRepo.listarTodos().size());
                System.out.println("[CargoRepository] listarTodos -> "
                        + cargoRepo.listarTodos().size());
                Funcionario adminFake = funcRepo.buscarPorLogin("patricia.gomes");
                if (adminFake != null) {
                    System.out.println("\nFuncionário admin sample: " + adminFake.getPessoa().getNomeCompleto()
                            + " | cargo=" + adminFake.getCargo().getNomeCargo()
                            + " | login=" + adminFake.getLogin());
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
                                medicoController, quartoRepo, cargoRepo).setVisible(true);
                    } else {
                        System.exit(0);
                    }
                }
            });
        });
    }
}
