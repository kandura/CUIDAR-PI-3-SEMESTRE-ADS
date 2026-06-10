package br.com.cuidar.view;

import br.com.cuidar.controller.FuncionarioController;
import br.com.cuidar.controller.MedicoController;
import br.com.cuidar.model.*;
import br.com.cuidar.repository.CargoRepository;
import br.com.cuidar.repository.QuartoRepository;
import br.com.cuidar.util.CpfUtil;
import br.com.cuidar.util.InputHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Painel administrativo do sistema CUIDAR.
 * Utiliza {@link JTabbedPane} com três abas:
 * <ul>
 *   <li>Funcionários — cadastro e listagem de {@link Funcionario}</li>
 *   <li>Médicos — cadastro e listagem de {@link Medico}</li>
 *   <li>Quartos — cadastro e listagem de {@link Quarto}</li>
 * </ul>
 */
public class ControleAdministrativoPanel extends JPanel {

    private enum EstadoCrud { NOVO, VIEW, EDIT }

    private final FuncionarioController funcionarioController;
    private final MedicoController medicoController;
    private final QuartoRepository quartoRepository;
    private final CargoRepository cargoRepository;
    private final Funcionario funcionarioLogado;

    private JTabbedPane tabbedPane;

    public ControleAdministrativoPanel(FuncionarioController funcionarioController,
                                      MedicoController medicoController,
                                      QuartoRepository quartoRepository,
                                      CargoRepository cargoRepository,
                                      Funcionario funcionarioLogado) {
        this.funcionarioController = funcionarioController;
        this.medicoController = medicoController;
        this.quartoRepository = quartoRepository;
        this.cargoRepository = cargoRepository;
        this.funcionarioLogado = funcionarioLogado;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initComponents();
    }

    private void initComponents() {
        boolean admin = isAdministrador();
        JLabel lblTitulo = new JLabel(admin ? "Controle Administrativo" : "Meu Perfil");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 153));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        add(lblTitulo, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        if (admin) {
            tabbedPane.addTab("Funcionários", criarPainelFuncionariosAdmin());
            tabbedPane.addTab("Médicos", criarPainelMedicos());
            tabbedPane.addTab("Quartos", criarPainelQuartos());
        } else {
            tabbedPane.addTab("Meu Perfil", criarPainelMeuPerfil());
        }
        add(tabbedPane, BorderLayout.CENTER);
    }

    private boolean isAdministrador() {
        return funcionarioLogado != null
                && funcionarioLogado.getCargo() != null
                && "Administrador".equalsIgnoreCase(funcionarioLogado.getCargo().getNomeCargo());
    }

    // ==================== ABA FUNCIONARIOS (MEU PERFIL - não admin) ====================
    private JPanel criarPainelMeuPerfil() {
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Aviso ===
        JLabel lblAviso = new JLabel("Você só pode visualizar e editar os seus próprios dados. Deixe a senha em branco para mantê-la.");
        lblAviso.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblAviso.setForeground(new Color(120, 120, 120));
        lblAviso.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // === Formulário ===
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)), "Meus Dados"),
                BorderFactory.createEmptyBorder(5, 8, 8, 8)));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 5, 3, 5);
        g.weightx = 1.0;

        // Linha 1: Nome | CPF | Data Nasc.
        addLabel(form, g, 0, 0, 1, "Nome:");
        addLabel(form, g, 1, 0, 1, "CPF:");
        addLabel(form, g, 2, 0, 1, "Data Nasc. (dd/MM/yyyy):");

        JTextField txtNome = new JTextField();
        JTextField txtCpf = new JTextField();
        JTextField txtNasc = new JTextField();
        InputHelper.aplicarMascaraCpf(txtCpf);
        InputHelper.aplicarMascaraData(txtNasc);
        txtCpf.setEditable(false);
        addField(form, g, 0, 1, 1, txtNome);
        addField(form, g, 1, 1, 1, txtCpf);
        addField(form, g, 2, 1, 1, txtNasc);

        // Linha 2: Sexo | Login | Senha
        addLabel(form, g, 0, 2, 1, "Sexo:");
        addLabel(form, g, 1, 2, 1, "Login:");
        addLabel(form, g, 2, 2, 1, "Senha:");

        JComboBox<String> cmbSexo = new JComboBox<>(new String[]{"Masculino", "Feminino"});
        JTextField txtLogin = new JTextField();
        JPasswordField txtSenha = new JPasswordField();
        txtLogin.setEditable(false);
        txtSenha.setToolTipText("Deixe em branco para manter a senha atual");
        addField(form, g, 0, 3, 1, cmbSexo);
        addField(form, g, 1, 3, 1, txtLogin);
        addField(form, g, 2, 3, 1, txtSenha);

        // Linha 3: Turno | Telefone | Email
        addLabel(form, g, 0, 4, 1, "Turno:");
        addLabel(form, g, 1, 4, 1, "Telefone:");
        addLabel(form, g, 2, 4, 1, "Email:");

        JComboBox<String> cmbTurno = new JComboBox<>(new String[]{"Manhã", "Tarde", "Noite"});
        JTextField txtTel = new JTextField();
        JTextField txtEmail = new JTextField();
        InputHelper.aplicarMascaraTelefone(txtTel);
        addField(form, g, 0, 5, 1, cmbTurno);
        addField(form, g, 1, 5, 1, txtTel);
        addField(form, g, 2, 5, 1, txtEmail);

        // Linha 4: Rua | Nº | CEP
        addLabel(form, g, 0, 6, 1, "Rua:");
        addLabel(form, g, 1, 6, 1, "Nº:");
        addLabel(form, g, 2, 6, 1, "CEP:");

        JTextField txtRua = new JTextField();
        JTextField txtNum = new JTextField();
        JTextField txtCep = new JTextField();
        InputHelper.aplicarApenasNumeros(txtNum);
        InputHelper.aplicarMascaraCep(txtCep);
        addField(form, g, 0, 7, 1, txtRua);
        addField(form, g, 1, 7, 1, txtNum);
        addField(form, g, 2, 7, 1, txtCep);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoes.setBackground(Color.WHITE);
        JButton btnSalvar = criarBotao("Salvar", new Color(0, 153, 76));
        JButton btnEditar = criarBotao("Editar", new Color(0, 102, 153));
        JButton btnDesfazer = criarBotao("Cancelar", new Color(150, 150, 150));
        botoes.add(btnSalvar);
        botoes.add(btnEditar);
        botoes.add(btnDesfazer);

        g.gridx = 0; g.gridy = 8; g.gridwidth = 3; g.anchor = GridBagConstraints.WEST;
        form.add(botoes, g);
        g.gridwidth = 1;

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Color.WHITE);
        topo.add(lblAviso, BorderLayout.NORTH);
        topo.add(form, BorderLayout.CENTER);
        painel.add(topo, BorderLayout.NORTH);

        // === Tabela (somente leitura — sem senha) ===
        JLabel lblOutros = new JLabel("Demais Funcionários (somente leitura)");
        lblOutros.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblOutros.setForeground(new Color(0, 102, 153));
        lblOutros.setBorder(BorderFactory.createEmptyBorder(8, 0, 5, 0));

        String[] cols = {"ID", "Nome", "CPF", "Cargo", "Login", "Turno", "Telefone"};
        DefaultTableModel tmFunc = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblFunc = new JTable(tmFunc);
        tblFunc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblFunc.setRowHeight(22);
        tblFunc.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblFunc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblFunc.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblFunc);

        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(Color.WHITE);
        tabelaPanel.add(lblOutros, BorderLayout.NORTH);
        tabelaPanel.add(scroll, BorderLayout.CENTER);
        painel.add(tabelaPanel, BorderLayout.CENTER);

        // === Carregar dados próprios + tabela ===
        Runnable preencherFormProprio = () -> {
            try {
                Funcionario eu = funcionarioController.listarTodos().stream()
                        .filter(f -> f.getId() == funcionarioLogado.getId())
                        .findFirst().orElse(funcionarioLogado);
                txtNome.setText(eu.getPessoa().getNomeCompleto());
                txtCpf.setText(eu.getPessoa().getCpf());
                txtNasc.setText(eu.getPessoa().getDataNascimento()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                cmbSexo.setSelectedItem(eu.getPessoa().getSexo());
                txtLogin.setText(eu.getLogin());
                txtSenha.setText("");
                cmbTurno.setSelectedItem(eu.getTurno());
                txtTel.setText(eu.getTelefone() != null ? eu.getTelefone() : "");
                txtEmail.setText(eu.getEmail() != null ? eu.getEmail() : "");
                txtRua.setText(eu.getRua() != null ? eu.getRua() : "");
                txtNum.setText(String.valueOf(eu.getNumero()));
                txtCep.setText(eu.getCep() != null ? eu.getCep() : "");
            } catch (Exception ex) { /* ignore */ }
        };

        Runnable carregarFunc = () -> {
            tmFunc.setRowCount(0);
            try {
                for (Funcionario f : funcionarioController.listarTodos()) {
                    tmFunc.addRow(new Object[]{
                            f.getId(),
                            f.getPessoa().getNomeCompleto(),
                            f.getPessoa().getCpf(),
                            f.getCargo() != null ? f.getCargo().getNomeCargo() : "",
                            f.getLogin(),
                            f.getTurno(),
                            f.getTelefone()
                    });
                }
            } catch (Exception ex) { /* sem conexão */ }
        };
        preencherFormProprio.run();
        carregarFunc.run();

        final boolean[] editando = {false};
        Runnable aplicarEstado = () -> {
            boolean edit = editando[0];
            txtNome.setEditable(edit);
            txtNasc.setEditable(edit);
            txtSenha.setEditable(edit);
            txtTel.setEditable(edit);
            txtEmail.setEditable(edit);
            txtRua.setEditable(edit);
            txtNum.setEditable(edit);
            txtCep.setEditable(edit);
            cmbSexo.setEnabled(edit);
            cmbTurno.setEnabled(edit);
            btnSalvar.setVisible(edit);
            btnEditar.setVisible(!edit);
            btnDesfazer.setVisible(edit);
        };
        aplicarEstado.run();

        btnEditar.addActionListener(e -> {
            editando[0] = true;
            aplicarEstado.run();
        });

        // Bloqueia clique em outros funcionários
        tblFunc.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tblFunc.getSelectedRow();
                if (row < 0) return;
                int id = (int) tmFunc.getValueAt(row, 0);
                if (id != funcionarioLogado.getId()) {
                    JOptionPane.showMessageDialog(painel,
                            "Você só pode editar os seus próprios dados.",
                            "Acesso restrito", JOptionPane.INFORMATION_MESSAGE);
                    tblFunc.clearSelection();
                }
            }
        });

        btnSalvar.addActionListener(e -> {
            String senha = new String(txtSenha.getPassword());
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(painel, "Nome é obrigatório.");
                return;
            }
            try {
                Funcionario eu = funcionarioController.listarTodos().stream()
                        .filter(f -> f.getId() == funcionarioLogado.getId())
                        .findFirst().orElse(null);
                if (eu == null) {
                    JOptionPane.showMessageDialog(painel, "Não foi possível localizar seu cadastro.");
                    return;
                }
                eu.getPessoa().setNomeCompleto(txtNome.getText().trim());
                eu.getPessoa().setSexo((String) cmbSexo.getSelectedItem());
                eu.getPessoa().setDataNascimento(
                        LocalDate.parse(txtNasc.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                if (!senha.isBlank()) {
                    eu.setSenha(senha.trim());
                }
                eu.setTurno((String) cmbTurno.getSelectedItem());
                eu.setTelefone(txtTel.getText().trim().replaceAll("[^0-9]", ""));
                eu.setEmail(txtEmail.getText().trim());
                eu.setRua(txtRua.getText().trim());
                eu.setNumero(txtNum.getText().trim().isEmpty() ? 0
                        : Integer.parseInt(txtNum.getText().trim().replaceAll("[^0-9]", "")));
                eu.setCep(txtCep.getText().trim().replaceAll("[^0-9]", ""));
                funcionarioController.editarFuncionario(eu);
                JOptionPane.showMessageDialog(painel, "Seus dados foram atualizados!");
                preencherFormProprio.run();
                carregarFunc.run();
                editando[0] = false;
                aplicarEstado.run();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(painel, "Data inválida. Use dd/MM/yyyy.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDesfazer.addActionListener(e -> {
            preencherFormProprio.run();
            editando[0] = false;
            aplicarEstado.run();
        });

        return painel;
    }

    // ==================== ABA FUNCIONARIOS (ADMIN - CRUD completo) ====================
    private JPanel criarPainelFuncionariosAdmin() {
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblAviso = new JLabel("Selecione um funcionário para visualizar. Use \"Editar\" para alterar ou \"Limpar\" para cadastrar um novo. Deixe a senha em branco ao editar para mantê-la.");
        lblAviso.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblAviso.setForeground(new Color(120, 120, 120));
        lblAviso.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)), "Dados do Funcionário"),
                BorderFactory.createEmptyBorder(5, 8, 8, 8)));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 5, 3, 5);
        g.weightx = 1.0;

        addLabel(form, g, 0, 0, 1, "Nome:");
        addLabel(form, g, 1, 0, 1, "CPF:");
        addLabel(form, g, 2, 0, 1, "Data Nasc. (dd/MM/yyyy):");

        JTextField txtNome = new JTextField();
        JTextField txtCpf = new JTextField();
        JTextField txtNasc = new JTextField();
        InputHelper.aplicarMascaraCpf(txtCpf);
        InputHelper.aplicarMascaraData(txtNasc);
        addField(form, g, 0, 1, 1, txtNome);
        addField(form, g, 1, 1, 1, txtCpf);
        addField(form, g, 2, 1, 1, txtNasc);

        addLabel(form, g, 0, 2, 1, "Sexo:");
        addLabel(form, g, 1, 2, 1, "Cargo:");
        addLabel(form, g, 2, 2, 1, "Turno:");

        JComboBox<String> cmbSexo = new JComboBox<>(new String[]{"Masculino", "Feminino"});
        JComboBox<Cargo> cmbCargo = new JComboBox<>();
        cmbCargo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cargo) setText(((Cargo) value).getNomeCargo());
                return this;
            }
        });
        try {
            for (Cargo c : cargoRepository.listarTodos()) cmbCargo.addItem(c);
        } catch (Exception ex) { /* sem conexão */ }
        JComboBox<String> cmbTurno = new JComboBox<>(new String[]{"Manhã", "Tarde", "Noite", "Diurno", "Noturno"});
        addField(form, g, 0, 3, 1, cmbSexo);
        addField(form, g, 1, 3, 1, cmbCargo);
        addField(form, g, 2, 3, 1, cmbTurno);

        addLabel(form, g, 0, 4, 1, "Login:");
        addLabel(form, g, 1, 4, 1, "Senha:");
        addLabel(form, g, 2, 4, 1, "Telefone:");

        JTextField txtLogin = new JTextField();
        JPasswordField txtSenha = new JPasswordField();
        txtSenha.setToolTipText("Deixe em branco ao editar para manter a senha atual");
        JTextField txtTel = new JTextField();
        InputHelper.aplicarMascaraTelefone(txtTel);
        addField(form, g, 0, 5, 1, txtLogin);
        addField(form, g, 1, 5, 1, txtSenha);
        addField(form, g, 2, 5, 1, txtTel);

        addLabel(form, g, 0, 6, 1, "Email:");
        addLabel(form, g, 1, 6, 1, "Rua:");
        addLabel(form, g, 2, 6, 1, "Nº / CEP:");

        JTextField txtEmail = new JTextField();
        JTextField txtRua = new JTextField();
        JPanel numCep = new JPanel(new GridLayout(1, 2, 5, 0));
        numCep.setBackground(Color.WHITE);
        JTextField txtNum = new JTextField();
        JTextField txtCep = new JTextField();
        InputHelper.aplicarApenasNumeros(txtNum);
        InputHelper.aplicarMascaraCep(txtCep);
        numCep.add(txtNum);
        numCep.add(txtCep);
        addField(form, g, 0, 7, 1, txtEmail);
        addField(form, g, 1, 7, 1, txtRua);
        addField(form, g, 2, 7, 1, numCep);

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoes.setBackground(Color.WHITE);
        JButton btnSalvar = criarBotao("Salvar", new Color(0, 153, 76));
        JButton btnEditar = criarBotao("Editar", new Color(0, 102, 153));
        JButton btnLimpar = criarBotao("Limpar", new Color(150, 150, 150));
        botoes.add(btnSalvar);
        botoes.add(btnEditar);
        botoes.add(btnLimpar);
        g.gridx = 0; g.gridy = 8; g.gridwidth = 3; g.anchor = GridBagConstraints.WEST;
        form.add(botoes, g);
        g.gridwidth = 1;

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Color.WHITE);
        topo.add(lblAviso, BorderLayout.NORTH);
        topo.add(form, BorderLayout.CENTER);
        painel.add(topo, BorderLayout.NORTH);

        // Tabela
        String[] cols = {"ID", "Nome", "CPF", "Cargo", "Login", "Turno", "Telefone"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tbl = new JTable(tm);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tbl.setRowHeight(22);
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        painel.add(new JScrollPane(tbl), BorderLayout.CENTER);

        final Funcionario[] selecionado = {null};
        final EstadoCrud[] estado = {EstadoCrud.NOVO};

        Runnable aplicarEstado = () -> {
            boolean editavel = (estado[0] == EstadoCrud.NOVO || estado[0] == EstadoCrud.EDIT);
            txtNome.setEditable(editavel);
            txtCpf.setEditable(estado[0] == EstadoCrud.NOVO);
            txtNasc.setEditable(editavel);
            txtLogin.setEditable(estado[0] == EstadoCrud.NOVO);
            txtSenha.setEditable(editavel);
            txtTel.setEditable(editavel);
            txtEmail.setEditable(editavel);
            txtRua.setEditable(editavel);
            txtNum.setEditable(editavel);
            txtCep.setEditable(editavel);
            cmbSexo.setEnabled(editavel);
            cmbCargo.setEnabled(editavel);
            cmbTurno.setEnabled(editavel);
            switch (estado[0]) {
                case NOVO:
                    btnSalvar.setText("Salvar"); btnSalvar.setVisible(true);
                    btnEditar.setVisible(false);
                    btnLimpar.setText("Limpar"); btnLimpar.setVisible(true);
                    break;
                case VIEW:
                    btnSalvar.setVisible(false);
                    btnEditar.setText("Editar"); btnEditar.setVisible(true);
                    btnLimpar.setText("Limpar"); btnLimpar.setVisible(true);
                    break;
                case EDIT:
                    btnSalvar.setText("Salvar"); btnSalvar.setVisible(true);
                    btnEditar.setVisible(false);
                    btnLimpar.setText("Cancelar"); btnLimpar.setVisible(true);
                    break;
            }
        };

        Runnable carregar = () -> {
            tm.setRowCount(0);
            try {
                for (Funcionario f : funcionarioController.listarTodos()) {
                    tm.addRow(new Object[]{
                            f.getId(),
                            f.getPessoa().getNomeCompleto(),
                            f.getPessoa().getCpf(),
                            f.getCargo() != null ? f.getCargo().getNomeCargo() : "",
                            f.getLogin(), f.getTurno(), f.getTelefone()
                    });
                }
            } catch (Exception ex) { /* sem conexão */ }
        };
        carregar.run();

        Runnable preencherDeSelecionado = () -> {
            Funcionario f = selecionado[0];
            if (f == null) return;
            txtNome.setText(f.getPessoa().getNomeCompleto());
            txtCpf.setText(f.getPessoa().getCpf());
            txtNasc.setText(f.getPessoa().getDataNascimento()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            cmbSexo.setSelectedItem(f.getPessoa().getSexo());
            if (f.getCargo() != null) {
                for (int i = 0; i < cmbCargo.getItemCount(); i++) {
                    if (cmbCargo.getItemAt(i).getId() == f.getCargo().getId()) {
                        cmbCargo.setSelectedIndex(i); break;
                    }
                }
            }
            cmbTurno.setSelectedItem(f.getTurno());
            txtLogin.setText(f.getLogin());
            txtSenha.setText("");
            txtTel.setText(f.getTelefone() != null ? f.getTelefone() : "");
            txtEmail.setText(f.getEmail() != null ? f.getEmail() : "");
            txtRua.setText(f.getRua() != null ? f.getRua() : "");
            txtNum.setText(String.valueOf(f.getNumero()));
            txtCep.setText(f.getCep() != null ? f.getCep() : "");
        };

        Runnable limparForm = () -> {
            selecionado[0] = null;
            txtNome.setText(""); txtCpf.setText(""); txtNasc.setText("");
            cmbSexo.setSelectedIndex(0);
            if (cmbCargo.getItemCount() > 0) cmbCargo.setSelectedIndex(0);
            cmbTurno.setSelectedIndex(0);
            txtLogin.setText(""); txtSenha.setText(""); txtTel.setText("");
            txtEmail.setText(""); txtRua.setText(""); txtNum.setText(""); txtCep.setText("");
            tbl.clearSelection();
            estado[0] = EstadoCrud.NOVO;
            aplicarEstado.run();
        };

        btnEditar.addActionListener(e -> {
            if (selecionado[0] == null) return;
            estado[0] = EstadoCrud.EDIT;
            aplicarEstado.run();
        });

        btnLimpar.addActionListener(e -> {
            if (estado[0] == EstadoCrud.EDIT && selecionado[0] != null) {
                preencherDeSelecionado.run();
                estado[0] = EstadoCrud.VIEW;
                aplicarEstado.run();
            } else {
                limparForm.run();
            }
        });

        tbl.getSelectionModel().addListSelectionListener(ev -> {
            if (ev.getValueIsAdjusting()) return;
            int row = tbl.getSelectedRow();
            if (row < 0) return;
            int id = (int) tm.getValueAt(row, 0);
            try {
                Funcionario f = funcionarioController.listarTodos().stream()
                        .filter(x -> x.getId() == id).findFirst().orElse(null);
                if (f == null) return;
                selecionado[0] = f;
                preencherDeSelecionado.run();
                estado[0] = EstadoCrud.VIEW;
                aplicarEstado.run();
            } catch (Exception ex) { /* sem conexão */ }
        });

        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String cpf = txtCpf.getText().replaceAll("[^0-9]", "");
            String senha = new String(txtSenha.getPassword()).trim();
            String login = txtLogin.getText().trim();
            if (nome.isEmpty() || cpf.isEmpty() || login.isEmpty()) {
                JOptionPane.showMessageDialog(painel, "Nome, CPF e Login são obrigatórios.");
                return;
            }
            if (!CpfUtil.isValid(cpf)) {
                JOptionPane.showMessageDialog(painel, "CPF inválido.");
                return;
            }
            Cargo cargoSel = (Cargo) cmbCargo.getSelectedItem();
            if (cargoSel == null) {
                JOptionPane.showMessageDialog(painel, "Selecione um cargo.");
                return;
            }
            try {
                LocalDate dn = LocalDate.parse(txtNasc.getText().trim(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int numero = txtNum.getText().trim().isEmpty() ? 0
                        : Integer.parseInt(txtNum.getText().trim().replaceAll("[^0-9]", ""));

                if (estado[0] == EstadoCrud.EDIT && selecionado[0] != null) {
                    Funcionario f = selecionado[0];
                    f.getPessoa().setNomeCompleto(nome);
                    f.getPessoa().setSexo((String) cmbSexo.getSelectedItem());
                    f.getPessoa().setDataNascimento(dn);
                    f.setCargo(cargoSel);
                    f.setTurno((String) cmbTurno.getSelectedItem());
                    f.setTelefone(txtTel.getText().trim().replaceAll("[^0-9]", ""));
                    f.setEmail(txtEmail.getText().trim());
                    f.setRua(txtRua.getText().trim());
                    f.setNumero(numero);
                    f.setCep(txtCep.getText().trim().replaceAll("[^0-9]", ""));
                    if (!senha.isEmpty()) f.setSenha(senha);
                    funcionarioController.editarFuncionario(f);
                    JOptionPane.showMessageDialog(painel, "Funcionário atualizado.");
                    carregar.run();
                    estado[0] = EstadoCrud.VIEW;
                    aplicarEstado.run();
                } else {
                    if (senha.isEmpty()) {
                        JOptionPane.showMessageDialog(painel, "Senha é obrigatória ao cadastrar novo funcionário.");
                        return;
                    }
                    Pessoa p = new Pessoa(nome, cpf, (String) cmbSexo.getSelectedItem(), dn);
                    Funcionario f = new Funcionario(p, cargoSel, login, senha,
                            (String) cmbTurno.getSelectedItem(),
                            txtTel.getText().trim().replaceAll("[^0-9]", ""),
                            txtEmail.getText().trim(), txtRua.getText().trim(), numero,
                            txtCep.getText().trim().replaceAll("[^0-9]", ""));
                    funcionarioController.cadastrarFuncionario(f);
                    JOptionPane.showMessageDialog(painel, "Funcionário cadastrado.");
                    limparForm.run();
                    carregar.run();
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(painel, "Data inválida. Use dd/MM/yyyy.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        aplicarEstado.run();
        return painel;
    }

    // ==================== ABA MEDICOS ====================
    private JPanel criarPainelMedicos() {
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 5, 3, 5);
        g.weightx = 1.0;

        addLabel(form, g, 0, 0, 1, "Nome:");
        addLabel(form, g, 1, 0, 1, "CPF:");
        addLabel(form, g, 2, 0, 1, "Data Nasc. (dd/MM/yyyy):");

        JTextField txtNome = new JTextField();
        JTextField txtCpf = new JTextField();
        JTextField txtNasc = new JTextField();
        InputHelper.aplicarMascaraCpf(txtCpf);
        InputHelper.aplicarMascaraData(txtNasc);
        addField(form, g, 0, 1, 1, txtNome);
        addField(form, g, 1, 1, 1, txtCpf);
        addField(form, g, 2, 1, 1, txtNasc);

        addLabel(form, g, 0, 2, 1, "Sexo:");
        addLabel(form, g, 1, 2, 1, "CRM:");
        addLabel(form, g, 2, 2, 1, "Especialidade:");

        JComboBox<String> cmbSexo = new JComboBox<>(new String[]{"Masculino", "Feminino"});
        JTextField txtCrm = new JTextField();
        JTextField txtEsp = new JTextField();
        addField(form, g, 0, 3, 1, cmbSexo);
        addField(form, g, 1, 3, 1, txtCrm);
        addField(form, g, 2, 3, 1, txtEsp);

        addLabel(form, g, 0, 4, 1, "Telefone:");
        addLabel(form, g, 1, 4, 2, "Email:");

        JTextField txtTel = new JTextField();
        JTextField txtEmail = new JTextField();
        InputHelper.aplicarMascaraTelefone(txtTel);
        addField(form, g, 0, 5, 1, txtTel);
        g.gridx = 1; g.gridy = 5; g.gridwidth = 2;
        form.add(makeField(txtEmail), g);
        g.gridwidth = 1;

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoes.setBackground(Color.WHITE);
        JButton btnSalvar = criarBotao("Salvar", new Color(0, 153, 76));
        JButton btnEditar = criarBotao("Editar", new Color(0, 102, 153));
        JButton btnLimpar = criarBotao("Limpar", new Color(150, 150, 150));
        botoes.add(btnSalvar);
        botoes.add(btnEditar);
        botoes.add(btnLimpar);
        g.gridx = 0; g.gridy = 6; g.gridwidth = 3; g.anchor = GridBagConstraints.WEST;
        form.add(botoes, g);
        g.gridwidth = 1;

        painel.add(form, BorderLayout.NORTH);

        String[] cols = {"ID", "Nome", "CRM", "Especialidade", "Telefone", "Email"};
        DefaultTableModel tmMed = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblMed = new JTable(tmMed);
        tblMed.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblMed.setRowHeight(22);
        tblMed.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblMed.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblMed);
        painel.add(scroll, BorderLayout.CENTER);

        Runnable carregarMed = () -> {
            tmMed.setRowCount(0);
            try {
                for (Medico m : medicoController.listarTodos()) {
                    tmMed.addRow(new Object[]{
                            m.getId(),
                            m.getPessoa().getNomeCompleto(),
                            m.getCrm(),
                            m.getEspecialidade(),
                            m.getTelefone(),
                            m.getEmail()
                    });
                }
            } catch (Exception ex) { /* sem conexão */ }
        };
        carregarMed.run();

        final Medico[] medSel = {null};
        final EstadoCrud[] estado = {EstadoCrud.NOVO};

        Runnable aplicarEstado = () -> {
            boolean editavel = (estado[0] == EstadoCrud.NOVO || estado[0] == EstadoCrud.EDIT);
            txtNome.setEditable(editavel);
            txtCpf.setEditable(estado[0] == EstadoCrud.NOVO);
            txtNasc.setEditable(editavel);
            txtCrm.setEditable(editavel);
            txtEsp.setEditable(editavel);
            txtTel.setEditable(editavel);
            txtEmail.setEditable(editavel);
            cmbSexo.setEnabled(editavel);
            switch (estado[0]) {
                case NOVO:
                    btnSalvar.setText("Salvar"); btnSalvar.setVisible(true);
                    btnEditar.setVisible(false);
                    btnLimpar.setText("Limpar"); btnLimpar.setVisible(true);
                    break;
                case VIEW:
                    btnSalvar.setVisible(false);
                    btnEditar.setText("Editar"); btnEditar.setVisible(true);
                    btnLimpar.setText("Limpar"); btnLimpar.setVisible(true);
                    break;
                case EDIT:
                    btnSalvar.setText("Salvar"); btnSalvar.setVisible(true);
                    btnEditar.setVisible(false);
                    btnLimpar.setText("Cancelar"); btnLimpar.setVisible(true);
                    break;
            }
        };

        Runnable preencherDeSelecionado = () -> {
            Medico m = medSel[0];
            if (m == null) return;
            txtNome.setText(m.getPessoa().getNomeCompleto());
            txtCpf.setText(m.getPessoa().getCpf());
            txtNasc.setText(m.getPessoa().getDataNascimento()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            cmbSexo.setSelectedItem(m.getPessoa().getSexo());
            txtCrm.setText(m.getCrm());
            txtEsp.setText(m.getEspecialidade());
            txtTel.setText(m.getTelefone() != null ? m.getTelefone() : "");
            txtEmail.setText(m.getEmail() != null ? m.getEmail() : "");
        };

        Runnable limparForm = () -> {
            txtNome.setText(""); txtCpf.setText(""); txtNasc.setText("");
            txtCrm.setText(""); txtEsp.setText(""); txtTel.setText(""); txtEmail.setText("");
            cmbSexo.setSelectedIndex(0);
            medSel[0] = null;
            tblMed.clearSelection();
            estado[0] = EstadoCrud.NOVO;
            aplicarEstado.run();
        };

        tblMed.getSelectionModel().addListSelectionListener(ev -> {
            if (ev.getValueIsAdjusting()) return;
            int row = tblMed.getSelectedRow();
            if (row < 0) return;
            int id = (int) tmMed.getValueAt(row, 0);
            try {
                medSel[0] = medicoController.listarTodos().stream()
                        .filter(m -> m.getId() == id).findFirst().orElse(null);
            } catch (Exception ex) { return; }
            if (medSel[0] == null) return;
            preencherDeSelecionado.run();
            estado[0] = EstadoCrud.VIEW;
            aplicarEstado.run();
        });

        btnEditar.addActionListener(e -> {
            if (medSel[0] == null) return;
            estado[0] = EstadoCrud.EDIT;
            aplicarEstado.run();
        });

        btnSalvar.addActionListener(e -> {
            if (txtNome.getText().trim().isEmpty() || txtCrm.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(painel, "Preencha Nome e CRM.");
                return;
            }
            if (!txtCpf.getText().trim().isEmpty() && !CpfUtil.isValid(txtCpf.getText().trim())) {
                JOptionPane.showMessageDialog(painel, "CPF inválido. Verifique os dígitos.");
                return;
            }
            try {
                if (estado[0] == EstadoCrud.EDIT && medSel[0] != null) {
                    medSel[0].getPessoa().setNomeCompleto(txtNome.getText().trim());
                    medSel[0].getPessoa().setSexo((String) cmbSexo.getSelectedItem());
                    medSel[0].getPessoa().setDataNascimento(
                            LocalDate.parse(txtNasc.getText().trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    medSel[0].setCrm(txtCrm.getText().trim());
                    medSel[0].setEspecialidade(txtEsp.getText().trim());
                    medSel[0].setTelefone(txtTel.getText().trim());
                    medSel[0].setEmail(txtEmail.getText().trim());
                    medicoController.editarMedico(medSel[0]);
                    JOptionPane.showMessageDialog(painel, "Médico atualizado!");
                    carregarMed.run();
                    estado[0] = EstadoCrud.VIEW;
                    aplicarEstado.run();
                } else {
                    Pessoa p = new Pessoa();
                    p.setNomeCompleto(txtNome.getText().trim());
                    p.setCpf(txtCpf.getText().trim().replaceAll("[^0-9]", ""));
                    p.setSexo((String) cmbSexo.getSelectedItem());
                    p.setDataNascimento(LocalDate.parse(txtNasc.getText().trim(),
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                    Medico m = new Medico();
                    m.setPessoa(p);
                    m.setCrm(txtCrm.getText().trim());
                    m.setEspecialidade(txtEsp.getText().trim());
                    m.setTelefone(txtTel.getText().trim().replaceAll("[^0-9]", ""));
                    m.setEmail(txtEmail.getText().trim());
                    medicoController.cadastrarMedico(m);
                    JOptionPane.showMessageDialog(painel, "Médico cadastrado!");
                    limparForm.run();
                    carregarMed.run();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpar.addActionListener(e -> {
            if (estado[0] == EstadoCrud.EDIT && medSel[0] != null) {
                preencherDeSelecionado.run();
                estado[0] = EstadoCrud.VIEW;
                aplicarEstado.run();
            } else {
                limparForm.run();
            }
        });

        aplicarEstado.run();
        return painel;
    }

    // ==================== ABA QUARTOS ====================
    private JPanel criarPainelQuartos() {
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 5, 3, 5);

        addLabel(form, g, 0, 0, 1, "Número:");
        addLabel(form, g, 1, 0, 1, "Status:");

        JTextField txtNumero = new JTextField();
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Disponível", "Ocupado", "Manutenção"});
        InputHelper.aplicarApenasNumeros(txtNumero);

        g.weightx = 0.3;
        addField(form, g, 0, 1, 1, txtNumero);
        g.weightx = 0.5;
        addField(form, g, 1, 1, 1, cmbStatus);
        g.weightx = 1.0;

        // Botões na mesma linha, alinhados à direita
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoes.setBackground(Color.WHITE);
        JButton btnSalvar = criarBotao("Salvar", new Color(0, 153, 76));
        JButton btnEditar = criarBotao("Editar", new Color(0, 102, 153));
        JButton btnLimpar = criarBotao("Limpar", new Color(150, 150, 150));
        botoes.add(btnSalvar);
        botoes.add(btnEditar);
        botoes.add(btnLimpar);
        g.gridx = 2; g.gridy = 1; g.anchor = GridBagConstraints.WEST;
        form.add(botoes, g);
        g.anchor = GridBagConstraints.CENTER;

        painel.add(form, BorderLayout.NORTH);

        String[] cols = {"ID", "Número", "Status"};
        DefaultTableModel tmQ = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblQ = new JTable(tmQ);
        tblQ.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblQ.setRowHeight(22);
        tblQ.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblQ.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblQ);
        painel.add(scroll, BorderLayout.CENTER);

        Runnable carregarQ = () -> {
            tmQ.setRowCount(0);
            try {
                for (Quarto q : quartoRepository.listarTodos()) {
                    tmQ.addRow(new Object[]{q.getId(), q.getNumero(), q.getStatus()});
                }
            } catch (Exception ex) { /* sem conexão */ }
        };
        carregarQ.run();

        final Quarto[] qSel = {null};
        final EstadoCrud[] estado = {EstadoCrud.NOVO};

        Runnable aplicarEstado = () -> {
            boolean editavel = (estado[0] == EstadoCrud.NOVO || estado[0] == EstadoCrud.EDIT);
            txtNumero.setEditable(editavel);
            cmbStatus.setEnabled(editavel);
            switch (estado[0]) {
                case NOVO:
                    btnSalvar.setText("Salvar"); btnSalvar.setVisible(true);
                    btnEditar.setVisible(false);
                    btnLimpar.setText("Limpar"); btnLimpar.setVisible(true);
                    break;
                case VIEW:
                    btnSalvar.setVisible(false);
                    btnEditar.setText("Editar"); btnEditar.setVisible(true);
                    btnLimpar.setText("Limpar"); btnLimpar.setVisible(true);
                    break;
                case EDIT:
                    btnSalvar.setText("Salvar"); btnSalvar.setVisible(true);
                    btnEditar.setVisible(false);
                    btnLimpar.setText("Cancelar"); btnLimpar.setVisible(true);
                    break;
            }
        };

        Runnable preencherDeSelecionado = () -> {
            Quarto q = qSel[0];
            if (q == null) return;
            txtNumero.setText(String.valueOf(q.getNumero()));
            cmbStatus.setSelectedItem(q.getStatus());
        };

        Runnable limparForm = () -> {
            txtNumero.setText("");
            cmbStatus.setSelectedIndex(0);
            qSel[0] = null;
            tblQ.clearSelection();
            estado[0] = EstadoCrud.NOVO;
            aplicarEstado.run();
        };

        tblQ.getSelectionModel().addListSelectionListener(ev -> {
            if (ev.getValueIsAdjusting()) return;
            int row = tblQ.getSelectedRow();
            if (row < 0) return;
            int id = (int) tmQ.getValueAt(row, 0);
            try {
                qSel[0] = quartoRepository.listarTodos().stream()
                        .filter(q -> q.getId() == id).findFirst().orElse(null);
            } catch (Exception ex) { return; }
            if (qSel[0] == null) return;
            preencherDeSelecionado.run();
            estado[0] = EstadoCrud.VIEW;
            aplicarEstado.run();
        });

        btnEditar.addActionListener(e -> {
            if (qSel[0] == null) return;
            estado[0] = EstadoCrud.EDIT;
            aplicarEstado.run();
        });

        btnSalvar.addActionListener(e -> {
            if (txtNumero.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(painel, "Informe o número do quarto.");
                return;
            }
            try {
                int numero = Integer.parseInt(txtNumero.getText().trim());
                if (estado[0] == EstadoCrud.EDIT && qSel[0] != null) {
                    qSel[0].setNumero(numero);
                    qSel[0].setStatus((String) cmbStatus.getSelectedItem());
                    quartoRepository.atualizar(qSel[0]);
                    JOptionPane.showMessageDialog(painel, "Quarto atualizado!");
                    carregarQ.run();
                    estado[0] = EstadoCrud.VIEW;
                    aplicarEstado.run();
                } else {
                    Quarto q = new Quarto();
                    q.setNumero(numero);
                    q.setStatus((String) cmbStatus.getSelectedItem());
                    quartoRepository.salvar(q);
                    JOptionPane.showMessageDialog(painel, "Quarto cadastrado!");
                    limparForm.run();
                    carregarQ.run();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(painel, "Número inválido.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(painel, "Erro: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLimpar.addActionListener(e -> {
            if (estado[0] == EstadoCrud.EDIT && qSel[0] != null) {
                preencherDeSelecionado.run();
                estado[0] = EstadoCrud.VIEW;
                aplicarEstado.run();
            } else {
                limparForm.run();
            }
        });

        aplicarEstado.run();
        return painel;
    }

    // ==================== HELPERS ====================
    private JLabel criarLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return lbl;
    }

    private JTextField criarTextField(int x, int y, int width) {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return txt;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(120, 32));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        return btn;
    }

    private void addLabel(JPanel form, GridBagConstraints g, int col, int row, int width, String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridx = col; g.gridy = row; g.gridwidth = width;
        form.add(lbl, g);
        g.gridwidth = 1;
    }

    private void addField(JPanel form, GridBagConstraints g, int col, int row, int width, JComponent comp) {
        if (comp instanceof JTextField) {
            ((JTextField) comp).setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
        comp.setPreferredSize(new Dimension(0, 28));
        g.gridx = col; g.gridy = row; g.gridwidth = width;
        form.add(comp, g);
        g.gridwidth = 1;
    }

    private JComponent makeField(JComponent comp) {
        if (comp instanceof JTextField) {
            ((JTextField) comp).setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
        comp.setPreferredSize(new Dimension(0, 28));
        return comp;
    }
}
