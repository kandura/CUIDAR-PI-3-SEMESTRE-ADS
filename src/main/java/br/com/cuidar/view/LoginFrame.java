package br.com.cuidar.view;

import br.com.cuidar.model.Funcionario;
import br.com.cuidar.service.LoginService;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de login do sistema CUIDAR.
 * Autentica o funcionário por login e senha utilizando o {@link LoginService}.
 * Após autenticação bem-sucedida, armazena o {@link Funcionario} logado.
 */
public class LoginFrame extends JFrame {

    private final LoginService loginService;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private Funcionario funcionarioLogado;

    public LoginFrame(LoginService loginService) {
        this.loginService = loginService;
        initComponents();
    }

    private void initComponents() {
        setTitle("CUIDAR - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 360);
        setMinimumSize(new Dimension(380, 320));
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(245, 245, 245));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel lblTitulo = new JLabel("Sistema CUIDAR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(0, 102, 153));
        painel.add(lblTitulo, gbc);

        gbc.gridy++;
        JLabel lblSubtitulo = new JLabel("Gestão de Instituição de Longa Permanência", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        painel.add(lblSubtitulo, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 2, 0);
        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painel.add(lblLogin, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        txtLogin = new JTextField();
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLogin.setPreferredSize(new Dimension(0, 32));
        painel.add(txtLogin, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        painel.add(lblSenha, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setPreferredSize(new Dimension(0, 32));
        painel.add(txtSenha, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 5, 0);
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setPreferredSize(new Dimension(0, 38));
        btnEntrar.setBackground(new Color(0, 102, 153));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setOpaque(true);
        btnEntrar.setContentAreaFilled(true);
        btnEntrar.setBorderPainted(false);
        btnEntrar.addActionListener(e -> realizarLogin());
        painel.add(btnEntrar, gbc);

        txtSenha.addActionListener(e -> realizarLogin());

        setContentPane(painel);
    }

    private void realizarLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha login e senha.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        funcionarioLogado = loginService.autenticar(login, senha);
        if (funcionarioLogado != null) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Login ou senha inválidos.",
                    "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
            txtSenha.requestFocus();
        }
    }

    public Funcionario getFuncionarioLogado() {
        return funcionarioLogado;
    }
}