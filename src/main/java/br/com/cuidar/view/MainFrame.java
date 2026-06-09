package br.com.cuidar.view;

import br.com.cuidar.model.Funcionario;

import javax.swing.*;
import java.awt.*;

/**
 * Frame principal do sistema CUIDAR.
 * Versão 3.3 (esqueleto): exibe a sidebar de navegação e troca placeholders
 * via {@link CardLayout}. Os painéis reais são plugados em v3.4/v3.5
 * (CadastroResidente, ControleAdministrativo, ControleMedicamento, GestaoAtividade,
 * Prontuário); a sidebar dinâmica por cargo (RBAC) e o fluxo de troca de conta
 * entram na v3.6.
 */
public class MainFrame extends JFrame {

    private final Funcionario funcionarioLogado;
    private final CardLayout cardLayout;
    private final JPanel painelConteudo;

    public MainFrame(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
        this.cardLayout = new CardLayout();
        this.painelConteudo = new JPanel(cardLayout);
        initComponents();
    }

    private void initComponents() {
        setTitle("CUIDAR - Sistema de Gestão ILPI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(0, 102, 153));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel lblLogo = new JLabel("CUIDAR", SwingConstants.CENTER);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        sidebar.add(lblLogo);

        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel lblUsuario = new JLabel("Olá, " + funcionarioLogado.getPessoa().getNomeCompleto().split(" ")[0],
                SwingConstants.CENTER);
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUsuario.setForeground(new Color(200, 220, 230));
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        sidebar.add(lblUsuario);

        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(0, 130, 180));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sidebar.add(sep);

        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        String[] menus = {"Residentes", "Medicamentos", "Atividades", "Prontuário", "Administrativo"};
        String[] cards = {"residentes", "medicamentos", "atividades", "prontuario", "administrativo"};
        for (int i = 0; i < menus.length; i++) {
            JButton btn = criarBotaoMenu(menus[i], cards[i]);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSair.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnSair.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSair.setBackground(new Color(180, 50, 50));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setOpaque(true);
        btnSair.setContentAreaFilled(true);
        btnSair.setBorderPainted(false);
        btnSair.addActionListener(e -> System.exit(0));
        sidebar.add(btnSair);

        add(sidebar, BorderLayout.WEST);

        // ===== PAINEIS DE CONTEUDO (placeholders v3.3) =====
        for (int i = 0; i < cards.length; i++) {
            painelConteudo.add(criarPlaceholder(menus[i]), cards[i]);
        }
        cardLayout.show(painelConteudo, "residentes");
        add(painelConteudo, BorderLayout.CENTER);
    }

    private JPanel criarPlaceholder(String titulo) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel(titulo + " — em construção");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lbl.setForeground(new Color(120, 120, 120));
        p.add(lbl);
        return p;
    }

    private JButton criarBotaoMenu(String texto, String card) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(new Color(0, 102, 153));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> cardLayout.show(painelConteudo, card));
        return btn;
    }
}
