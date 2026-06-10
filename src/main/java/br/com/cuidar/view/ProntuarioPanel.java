package br.com.cuidar.view;

import br.com.cuidar.controller.*;
import br.com.cuidar.model.*;
import br.com.cuidar.util.InputHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Painel do prontuário médico do {@link Residente}.
 * Permite buscar residente por CPF, visualizar/editar o prontuário
 * e consultar o histórico de {@link RegistroClinico}.
 * Inclui dialog para adicionar novos registros clínicos.
 */
public class ProntuarioPanel extends JPanel {

    private final ProntuarioController prontuarioController;
    private final RegistroClinicoController registroController;
    private final ResidenteController residenteController;
    private final MedicamentoController medicamentoController;
    private final MedicoController medicoController;
    private final Funcionario funcionarioLogado;

    private JTextField txtBuscaCpf;
    private JTextField txtPeso, txtAltura, txtAlergias, txtDosagem, txtIntercorrencia;
    private JComboBox<String> cmbTipoSanguineo, cmbTipoEvento;
    private JComboBox<Medicamento> cmbMedicamento;
    private JComboBox<Medico> cmbMedicoReg;
    private JTextArea txtObsProntuario;
    private JTable tabelaRegistros;
    private DefaultTableModel tableModelRegistros;
    private JLabel lblResidenteInfo;
    private Residente residenteAtual;
    private Prontuario prontuarioAtual;

    private enum EstadoProntuario { VAZIO, NOVO, VIEW, EDIT }
    private EstadoProntuario estadoPron = EstadoProntuario.VAZIO;
    private JButton btnSalvarPron, btnEditarPron, btnCancelarPron;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProntuarioPanel(ProntuarioController prontuarioController,
                           RegistroClinicoController registroController,
                           ResidenteController residenteController,
                           MedicamentoController medicamentoController,
                           MedicoController medicoController,
                           Funcionario funcionarioLogado) {
        this.prontuarioController = prontuarioController;
        this.registroController = registroController;
        this.residenteController = residenteController;
        this.medicamentoController = medicamentoController;
        this.medicoController = medicoController;
        this.funcionarioLogado = funcionarioLogado;
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        initComponents();
        carregarCombosRegistro();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Prontuário do Residente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 153));

        // ===== TOPO: título + busca + secções de formulário =====
        JPanel topo = new JPanel();
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        topo.setBackground(Color.WHITE);

        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setBackground(Color.WHITE);
        tituloPanel.add(lblTitulo, BorderLayout.WEST);
        tituloPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topo.add(tituloPanel);
        topo.add(Box.createRigidArea(new Dimension(0, 8)));

        // === BUSCA ===
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        buscaPanel.setBackground(Color.WHITE);
        buscaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblCpf = new JLabel("CPF do Residente:");
        lblCpf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBuscaCpf = new JTextField(15);
        txtBuscaCpf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        InputHelper.aplicarMascaraCpf(txtBuscaCpf);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.setPreferredSize(new Dimension(90, 28));
        btnBuscar.setBackground(new Color(0, 102, 153));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setOpaque(true);
        btnBuscar.setContentAreaFilled(true);
        btnBuscar.setBorderPainted(false);
        btnBuscar.addActionListener(e -> buscarResidente());

        lblResidenteInfo = new JLabel("");
        lblResidenteInfo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblResidenteInfo.setForeground(new Color(0, 80, 120));

        buscaPanel.add(lblCpf);
        buscaPanel.add(txtBuscaCpf);
        buscaPanel.add(btnBuscar);
        buscaPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buscaPanel.add(lblResidenteInfo);
        topo.add(buscaPanel);

        // === PRONTUÁRIO ===
        JPanel pronPanel = new JPanel(new GridBagLayout());
        pronPanel.setBackground(Color.WHITE);
        pronPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)), "Dados do Prontuário"),
                BorderFactory.createEmptyBorder(5, 8, 8, 8)));
        pronPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 5, 3, 5);
        g.weightx = 1.0;

        addLabel(pronPanel, g, 0, 0, "Peso (kg):");
        addLabel(pronPanel, g, 1, 0, "Altura (m):");
        addLabel(pronPanel, g, 2, 0, "Tipo Sanguíneo:");
        addLabel(pronPanel, g, 3, 0, "Alergias:");

        txtPeso = new JTextField();
        txtAltura = new JTextField();
        cmbTipoSanguineo = new JComboBox<>(new String[]{"", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        txtAlergias = new JTextField();
        InputHelper.aplicarApenasDecimal(txtPeso);
        InputHelper.aplicarApenasDecimal(txtAltura);

        addField(pronPanel, g, 0, 1, txtPeso);
        addField(pronPanel, g, 1, 1, txtAltura);
        addField(pronPanel, g, 2, 1, cmbTipoSanguineo);
        g.gridx = 3; g.gridy = 1; g.weightx = 2.0;
        pronPanel.add(makeField(txtAlergias), g);
        g.weightx = 1.0;

        addLabel(pronPanel, g, 0, 2, "Obs. Geral:");
        txtObsProntuario = new JTextArea(2, 20);
        txtObsProntuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObsProntuario.setLineWrap(true);
        txtObsProntuario.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(txtObsProntuario);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 4; g.fill = GridBagConstraints.BOTH; g.weighty = 0.2;
        pronPanel.add(scrollObs, g);
        g.gridwidth = 1; g.weighty = 0; g.fill = GridBagConstraints.HORIZONTAL;

        JButton btnSalvarPron = criarBotao("Salvar", new Color(0, 153, 76), 120);
        btnSalvarPron.addActionListener(e -> salvarProntuario());
        JButton btnEditarPron = criarBotao("Editar", new Color(0, 102, 153), 120);
        btnEditarPron.addActionListener(e -> entrarEdicaoProntuario());
        JButton btnCancelarPron = criarBotao("Cancelar", new Color(150, 150, 150), 120);
        btnCancelarPron.addActionListener(e -> cancelarEdicaoProntuario());
        this.btnSalvarPron = btnSalvarPron;
        this.btnEditarPron = btnEditarPron;
        this.btnCancelarPron = btnCancelarPron;
        JPanel botaoPronPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botaoPronPanel.setBackground(Color.WHITE);
        botaoPronPanel.add(btnSalvarPron);
        botaoPronPanel.add(btnEditarPron);
        botaoPronPanel.add(btnCancelarPron);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 4; g.anchor = GridBagConstraints.WEST;
        pronPanel.add(botaoPronPanel, g);
        g.gridwidth = 1;

        topo.add(Box.createRigidArea(new Dimension(0, 8)));
        topo.add(pronPanel);

        // === NOVO REGISTRO CLÍNICO ===
        JPanel regPanel = new JPanel(new GridBagLayout());
        regPanel.setBackground(Color.WHITE);
        regPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)), "Novo Registro Clínico"),
                BorderFactory.createEmptyBorder(5, 8, 8, 8)));
        regPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints g2 = new GridBagConstraints();
        g2.fill = GridBagConstraints.HORIZONTAL;
        g2.insets = new Insets(3, 5, 3, 5);
        g2.weightx = 1.0;

        addLabel(regPanel, g2, 0, 0, "Tipo Evento:");
        addLabel(regPanel, g2, 1, 0, "Medicamento:");
        addLabel(regPanel, g2, 2, 0, "Dosagem:");

        cmbTipoEvento = new JComboBox<>(new String[]{"Consulta", "Medicação", "Intercorrência", "Exame", "Outro"});
        cmbMedicamento = new JComboBox<>();
        txtDosagem = new JTextField();

        addField(regPanel, g2, 0, 1, cmbTipoEvento);
        addField(regPanel, g2, 1, 1, cmbMedicamento);
        addField(regPanel, g2, 2, 1, txtDosagem);

        addLabel(regPanel, g2, 0, 2, "Médico:");
        addLabel(regPanel, g2, 1, 2, "Intercorrência:");

        cmbMedicoReg = new JComboBox<>();
        txtIntercorrencia = new JTextField();

        addField(regPanel, g2, 0, 3, cmbMedicoReg);
        g2.gridx = 1; g2.gridy = 3; g2.gridwidth = 2;
        regPanel.add(makeField(txtIntercorrencia), g2);
        g2.gridwidth = 1;

        JPanel botoesReg = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoesReg.setBackground(Color.WHITE);
        JButton btnSalvarReg = criarBotao("Salvar Registro", new Color(0, 102, 153), 150);
        btnSalvarReg.addActionListener(e -> salvarRegistroClinico());
        JButton btnExcluirReg = criarBotao("Excluir Registro", new Color(180, 50, 50), 150);
        btnExcluirReg.addActionListener(e -> excluirRegistroClinico());
        botoesReg.add(btnSalvarReg);
        botoesReg.add(btnExcluirReg);
        g2.gridx = 0; g2.gridy = 4; g2.gridwidth = 3; g2.anchor = GridBagConstraints.WEST;
        regPanel.add(botoesReg, g2);

        topo.add(Box.createRigidArea(new Dimension(0, 8)));
        topo.add(regPanel);

        // ===== CENTRO: Tabela de Registros =====
        JPanel centro = new JPanel(new BorderLayout(0, 5));
        centro.setBackground(Color.WHITE);

        JLabel lblRegistros = new JLabel("Histórico de Registros Clínicos");
        lblRegistros.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRegistros.setBorder(BorderFactory.createEmptyBorder(8, 0, 5, 0));
        centro.add(lblRegistros, BorderLayout.NORTH);

        String[] colunas = {"ID", "Data", "Tipo Evento", "Medicamento", "Dosagem", "Médico", "Intercorrência"};
        tableModelRegistros = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaRegistros = new JTable(tableModelRegistros);
        tabelaRegistros.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaRegistros.setRowHeight(22);
        tabelaRegistros.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaRegistros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaRegistros.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollRegistros = new JScrollPane(tabelaRegistros);
        centro.add(scrollRegistros, BorderLayout.CENTER);

        add(topo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        aplicarEstadoProntuario(EstadoProntuario.VAZIO);
    }

    private void aplicarEstadoProntuario(EstadoProntuario novo) {
        this.estadoPron = novo;
        boolean editavel = (novo == EstadoProntuario.NOVO || novo == EstadoProntuario.EDIT);
        txtPeso.setEditable(editavel);
        txtAltura.setEditable(editavel);
        cmbTipoSanguineo.setEnabled(editavel);
        txtAlergias.setEditable(editavel);
        txtObsProntuario.setEditable(editavel);
        switch (novo) {
            case VAZIO:
                btnSalvarPron.setVisible(false);
                btnEditarPron.setVisible(false);
                btnCancelarPron.setVisible(false);
                break;
            case NOVO:
                btnSalvarPron.setText("Salvar"); btnSalvarPron.setVisible(true);
                btnEditarPron.setVisible(false);
                btnCancelarPron.setVisible(false);
                break;
            case VIEW:
                btnSalvarPron.setVisible(false);
                btnEditarPron.setVisible(true);
                btnCancelarPron.setVisible(false);
                break;
            case EDIT:
                btnSalvarPron.setText("Salvar"); btnSalvarPron.setVisible(true);
                btnEditarPron.setVisible(false);
                btnCancelarPron.setVisible(true);
                break;
        }
    }

    private void entrarEdicaoProntuario() {
        if (prontuarioAtual == null) return;
        aplicarEstadoProntuario(EstadoProntuario.EDIT);
    }

    private void cancelarEdicaoProntuario() {
        if (prontuarioAtual == null) {
            aplicarEstadoProntuario(EstadoProntuario.VAZIO);
            return;
        }
        txtPeso.setText(String.valueOf(prontuarioAtual.getPeso()));
        txtAltura.setText(String.valueOf(prontuarioAtual.getAltura()));
        cmbTipoSanguineo.setSelectedItem(prontuarioAtual.getTipoSanguineo() != null ? prontuarioAtual.getTipoSanguineo() : "");
        txtAlergias.setText(prontuarioAtual.getAlergias() != null ? prontuarioAtual.getAlergias() : "");
        txtObsProntuario.setText(prontuarioAtual.getObsGeral() != null ? prontuarioAtual.getObsGeral() : "");
        aplicarEstadoProntuario(EstadoProntuario.VIEW);
    }

    private void addLabel(JPanel form, GridBagConstraints g, int col, int row, String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridx = col; g.gridy = row;
        form.add(lbl, g);
    }

    private void addField(JPanel form, GridBagConstraints g, int col, int row, JComponent comp) {
        if (comp instanceof JTextField) {
            ((JTextField) comp).setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
        comp.setPreferredSize(new Dimension(0, 28));
        g.gridx = col; g.gridy = row;
        form.add(comp, g);
    }

    private JComponent makeField(JComponent comp) {
        if (comp instanceof JTextField) {
            ((JTextField) comp).setFont(new Font("Segoe UI", Font.PLAIN, 12));
        }
        comp.setPreferredSize(new Dimension(0, 28));
        return comp;
    }

    private void buscarResidente() {
        String cpf = txtBuscaCpf.getText().trim().replaceAll("[^0-9]", "");
        if (cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o CPF.");
            return;
        }
        try {
            residenteAtual = residenteController.buscarPorCpf(cpf);
            if (residenteAtual == null) {
                JOptionPane.showMessageDialog(this, "Residente não encontrado.");
                lblResidenteInfo.setText("");
                limparTodosCampos();
                return;
            }
            lblResidenteInfo.setText(residenteAtual.getPessoa().getNomeCompleto()
                    + " | Quarto " + residenteAtual.getQuarto().getNumero());
            carregarProntuario();
            carregarRegistros();
            carregarCombosRegistro();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarProntuario() {
        // Limpa campos primeiro para garantir que dados antigos não fiquem
        txtPeso.setText("");
        txtAltura.setText("");
        cmbTipoSanguineo.setSelectedIndex(0);
        txtAlergias.setText("");
        txtObsProntuario.setText("");
        prontuarioAtual = null;

        try {
            prontuarioAtual = prontuarioController.buscarPorResidente(residenteAtual);
            if (prontuarioAtual != null) {
                txtPeso.setText(String.valueOf(prontuarioAtual.getPeso()));
                txtAltura.setText(String.valueOf(prontuarioAtual.getAltura()));
                cmbTipoSanguineo.setSelectedItem(prontuarioAtual.getTipoSanguineo() != null ? prontuarioAtual.getTipoSanguineo() : "");
                txtAlergias.setText(prontuarioAtual.getAlergias() != null ? prontuarioAtual.getAlergias() : "");
                txtObsProntuario.setText(prontuarioAtual.getObsGeral() != null ? prontuarioAtual.getObsGeral() : "");
                aplicarEstadoProntuario(EstadoProntuario.VIEW);
            } else {
                aplicarEstadoProntuario(EstadoProntuario.NOVO);
            }
        } catch (Exception ex) {
            // ignore
        }
    }

    private void carregarRegistros() {
        tableModelRegistros.setRowCount(0);
        try {
            List<RegistroClinico> registros = registroController.listarPorResidente(residenteAtual);
            for (RegistroClinico rc : registros) {
                tableModelRegistros.addRow(new Object[]{
                        rc.getId(),
                        rc.getDataRegistro().format(FMT),
                        rc.getTipoEvento(),
                        rc.getMedicamento() != null ? rc.getMedicamento().getNome() : "",
                        rc.getDosagem(),
                        rc.getMedico() != null ? rc.getMedico().getPessoa().getNomeCompleto() : "",
                        rc.getIntercorrencia() != null ? rc.getIntercorrencia() : ""
                });
            }
        } catch (Exception ex) {
            // ignore
        }
    }

    private void salvarProntuario() {
        if (residenteAtual == null) {
            JOptionPane.showMessageDialog(this, "Busque um residente primeiro.");
            return;
        }
        try {
            double peso = Double.parseDouble(txtPeso.getText().trim().replace(",", "."));
            double altura = Double.parseDouble(txtAltura.getText().trim().replace(",", "."));

            // Recarrega do banco caso prontuarioAtual esteja null por erro anterior
            if (prontuarioAtual == null) {
                prontuarioAtual = prontuarioController.buscarPorResidente(residenteAtual);
            }

            if (prontuarioAtual == null) {
                Prontuario p = new Prontuario();
                p.setResidente(residenteAtual);
                p.setPeso(peso);
                p.setAltura(altura);
                p.setTipoSanguineo((String) cmbTipoSanguineo.getSelectedItem());
                p.setAlergias(txtAlergias.getText().trim());
                p.setObsGeral(txtObsProntuario.getText().trim());
                prontuarioController.criarProntuario(p);
                prontuarioAtual = p;
                JOptionPane.showMessageDialog(this, "Prontuário criado com sucesso!");
                aplicarEstadoProntuario(EstadoProntuario.VIEW);
            } else {
                prontuarioAtual.setPeso(peso);
                prontuarioAtual.setAltura(altura);
                prontuarioAtual.setTipoSanguineo((String) cmbTipoSanguineo.getSelectedItem());
                prontuarioAtual.setAlergias(txtAlergias.getText().trim());
                prontuarioAtual.setObsGeral(txtObsProntuario.getText().trim());
                prontuarioController.atualizarProntuario(prontuarioAtual);
                JOptionPane.showMessageDialog(this, "Prontuário atualizado com sucesso!");
                aplicarEstadoProntuario(EstadoProntuario.VIEW);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Peso e Altura devem ser numéricos.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCombosRegistro() {
        cmbMedicamento.removeAllItems();
        try {
            for (Medicamento m : medicamentoController.listarTodos()) {
                cmbMedicamento.addItem(m);
            }
        } catch (Exception ex) { /* ignore */ }

        cmbMedicoReg.removeAllItems();
        cmbMedicoReg.addItem(null);
        try {
            for (Medico med : medicoController.listarTodos()) {
                cmbMedicoReg.addItem(med);
            }
        } catch (Exception ex) { /* ignore */ }
    }

    private void limparTodosCampos() {
        txtPeso.setText("");
        txtAltura.setText("");
        cmbTipoSanguineo.setSelectedIndex(0);
        txtAlergias.setText("");
        txtObsProntuario.setText("");
        txtDosagem.setText("");
        txtIntercorrencia.setText("");
        tableModelRegistros.setRowCount(0);
        prontuarioAtual = null;
        aplicarEstadoProntuario(EstadoProntuario.VAZIO);
    }

    private void salvarRegistroClinico() {
        if (residenteAtual == null) {
            JOptionPane.showMessageDialog(this, "Busque um residente primeiro.");
            return;
        }
        try {
            RegistroClinico rc = new RegistroClinico();
            rc.setResidente(residenteAtual);
            rc.setFuncionario(funcionarioLogado);
            rc.setMedicamento((Medicamento) cmbMedicamento.getSelectedItem());
            rc.setMedico((Medico) cmbMedicoReg.getSelectedItem());
            rc.setTipoEvento((String) cmbTipoEvento.getSelectedItem());
            rc.setDosagem(txtDosagem.getText().trim());
            rc.setIntercorrencia(txtIntercorrencia.getText().trim());
            rc.setDataRegistro(java.time.LocalDate.now());

            registroController.adicionarRegistro(rc);
            JOptionPane.showMessageDialog(this, "Registro salvo com sucesso!");
            txtDosagem.setText("");
            txtIntercorrencia.setText("");
            carregarRegistros();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirRegistroClinico() {
        int row = tabelaRegistros.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um registro na tabela.");
            return;
        }
        int id = (int) tableModelRegistros.getValueAt(row, 0);
        int op = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o registro clínico #" + id + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;
        try {
            registroController.excluirRegistro(id);
            JOptionPane.showMessageDialog(this, "Registro excluído!");
            carregarRegistros();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    private JButton criarBotao(String texto, Color cor, int width) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(width, 32));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        return btn;
    }
}
