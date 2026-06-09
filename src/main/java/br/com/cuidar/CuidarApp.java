package br.com.cuidar;

import br.com.cuidar.model.Funcionario;
import br.com.cuidar.repository.FuncionarioRepository;
import br.com.cuidar.repository.impl.FuncionarioRepositoryImpl;
import br.com.cuidar.service.LoginService;
import br.com.cuidar.view.LoginFrame;
import br.com.cuidar.view.MainFrame;

import javax.swing.*;
import java.awt.GraphicsEnvironment;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 3.3 — primeira GUI Swing: {@code LoginFrame} autentica via
 * {@code LoginService} e, em sucesso, abre o {@code MainFrame} esqueleto
 * (sidebar com placeholders). Os painéis reais entram em v3.4/v3.5.
 *
 * <p>Sem argumentos: tenta abrir a GUI. Em ambiente sem display gráfico
 * (CI / validação automatizada), faz fallback para um smoke-test
 * "headless" que apenas exercita o {@link LoginService} contra o banco real.</p>
 */
public class CuidarApp {

    public static void main(String[] args) {
        boolean forceHeadless = args.length > 0 && "--headless".equalsIgnoreCase(args[0]);
        boolean headless = forceHeadless || GraphicsEnvironment.isHeadless();

        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 3.3 — LoginFrame + MainFrame (esqueleto)");

        FuncionarioRepository funcRepo = new FuncionarioRepositoryImpl();
        LoginService loginService = new LoginService(funcRepo);

        if (headless) {
            System.out.println("[modo headless] sem display — exercitando LoginService.\n");
            try {
                java.util.List<Funcionario> funcs = funcRepo.listarTodos();
                System.out.println("Funcionários disponíveis para login (" + funcs.size() + "):");
                for (Funcionario f : funcs) {
                    System.out.println("  - login=" + f.getLogin()
                            + " | nome=" + f.getPessoa().getNomeCompleto()
                            + " | cargo=" + (f.getCargo() != null ? f.getCargo().getNomeCargo() : "?"));
                }
                if (!funcs.isEmpty()) {
                    Funcionario primeiro = funcs.get(0);
                    Funcionario auth = loginService.autenticar(primeiro.getLogin(), primeiro.getSenha());
                    System.out.println("\nautenticar('" + primeiro.getLogin() + "', senha-real) -> "
                            + (auth != null ? "OK (" + auth.getPessoa().getNomeCompleto() + ")" : "FALHA"));
                    Funcionario bad = loginService.autenticar(primeiro.getLogin(), "errada_xyz");
                    System.out.println("autenticar('" + primeiro.getLogin() + "', errada) -> "
                            + (bad != null ? "OK" : "negado (esperado)"));
                }
                System.out.println("\n[modo headless] OK. Rode sem --headless num desktop para abrir a GUI.");
            } catch (RuntimeException e) {
                System.err.println("Falha ao consultar funcionários: " + e.getMessage());
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
            // bloqueia até o login fechar (modal "improvisado" via loop de visibilidade):
            login.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    Funcionario f = login.getFuncionarioLogado();
                    if (f != null) {
                        new MainFrame(f).setVisible(true);
                    } else {
                        System.exit(0);
                    }
                }
            });
        });
    }
}
