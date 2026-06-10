package br.com.cuidar.view;

import br.com.cuidar.controller.MedicamentoController;
import br.com.cuidar.model.Medicamento;
import br.com.cuidar.util.InputHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Painel de controle de {@link Medicamento}.
 * Permite cadastrar, editar e listar medicamentos.
 * Possui filtro em tempo real por nome com {@link java.awt.event.KeyListener}.
 */
public class ControleMedicamentoPanel extends JPanel {

    private enum Estado { NOVO, VIEW, EDIT }

    private final MedicamentoController controller;

    private JTextField txtNome, txtFabricante, txtValidade, txtQuantidade, txtFiltro;
    private JTextArea txtDescricao;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private Medicamento medicamentoSelecionado;

    private JButton btnSalvar, btnEditar, btnLimpar;
    private Estado estado = Estado.NOVO;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ControleMedicamentoPanel(MedicamentoController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Controle de Medicamentos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 153));

        JPanel topo = new JPanel(new BorderLayout(0, 10));
        topo.setBackground(Color.WHITE);
        topo.add(lblTitulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 5, 3, 5);
        g.weightx = 1.0;

        // Linha 1: Nome | Fabricante | Validade
        addLabel(form, g, 0, 0, "Nome:");
        addLabel(form, g, 1, 0, "Fabricante:");
        addLabel(form, g, 2, 0, "Validade (dd/MM/yyyy):");

        txtNome = new JTextField();
        txtFabricante = new JTextField();
        txtValidade = new JTextField();
        InputHelper.aplicarMascaraData(txtValidade);
        addField(form, g, 0, 1, txtNome);
        addField(form, g, 1, 1, txtFabricante);
        addField(form, g, 2, 1, txtValidade);

        // Linha 2: Quantidade | Descrição (span 2)
        addLabel(form, g, 0, 2, "Quantidade:");
        addLabel(form, g, 1, 2, "Descrição:");

        txtQuantidade = new JTextField();
        InputHelper.aplicarApenasNumeros(txtQuantidade);
        addField(form, g, 0, 3, txtQuantidade);

        txtDescricao = new JTextArea(2, 20);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        g.gridx = 1; g.gridy = 3; g.gridwidth = 2; g.fill = GridBagConstraints.BOTH; g.weighty = 0.3;
        form.add(scrollDesc, g);
        g.gridwidth = 1; g.weighty = 0; g.fill = GridBagConstraints.HORIZONTAL;

        // Botoes
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoes.setBackground(Color.WHITE);
        btnSalvar = criarBotao("Salvar", new Color(0, 153, 76));
        btnSalvar.addActionListener(e -> salvar());
        btnEditar = criarBotao("Editar", new Color(0, 102, 153));
        btnEditar.addActionListener(e -> entrarModoEdicao());
        btnLimpar = criarBotao("Limpar", new Color(150, 150, 150));
        btnLimpar.addActionListener(e -> limparFormulario());
        botoes.add(btnSalvar);
        botoes.add(btnEditar);
        botoes.add(btnLimpar);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 3; g.anchor = GridBagConstraints.WEST;
        form.add(botoes, g);
        g.gridwidth = 1;

        topo.add(form, BorderLayout.CENTER);

        // Centro: filtro + tabela
        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setBackground(Color.WHITE);

        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        filtroPanel.setBackground(Color.WHITE);
        JLabel lblFiltro = new JLabel("Filtrar por nome:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtFiltro = new JTextField(25);
        txtFiltro.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrar();
            }
        });
        filtroPanel.add(lblFiltro);
        filtroPanel.add(txtFiltro);

        String[] colunas = {"ID", "Nome", "Fabricante", "Validade", "Qtd", "Descrição"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(tableModel);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.setRowHeight(22);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                preencherFormulario();
            }
        });

        JScrollPane scrollTabela = new JScrollPane(tabela);
        centro.add(filtroPanel, BorderLayout.NORTH);
        centro.add(scrollTabela, BorderLayout.CENTER);

        add(topo, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        aplicarEstado(Estado.NOVO);
    }

    private void aplicarEstado(Estado novo) {
        this.estado = novo;
        boolean editavel = (novo == Estado.NOVO || novo == Estado.EDIT);
        txtNome.setEditable(editavel);
        txtFabricante.setEditable(editavel);
        txtValidade.setEditable(editavel);
        txtQuantidade.setEditable(editavel);
        txtDescricao.setEditable(editavel);

        switch (novo) {
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
    }

    private void entrarModoEdicao() {
        if (medicamentoSelecionado == null) return;
        aplicarEstado(Estado.EDIT);
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

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Medicamento> lista = controller.listarTodos();
            popularTabela(lista);
        } catch (Exception ex) {
            // sem conexão
        }
    }

    private void popularTabela(List<Medicamento> lista) {
        tableModel.setRowCount(0);
        for (Medicamento m : lista) {
            tableModel.addRow(new Object[]{
                    m.getId(),
                    m.getNome(),
                    m.getFabricante(),
                    m.getDataValidade().format(FMT),
                    m.getQuantidade(),
                    m.getDescricao()
            });
        }
    }

    private void filtrar() {
        String filtro = txtFiltro.getText().trim().toLowerCase();
        try {
            List<Medicamento> lista = controller.listarTodos();
            if (!filtro.isEmpty()) {
                lista = lista.stream()
                        .filter(m -> m.getNome().toLowerCase().contains(filtro))
                        .collect(Collectors.toList());
            }
            popularTabela(lista);
        } catch (Exception ex) {
            // ignore
        }
    }

    private void preencherFormulario() {
        int row = tabela.getSelectedRow();
        if (row < 0) return;

        int id = (int) tableModel.getValueAt(row, 0);
        try {
            List<Medicamento> todos = controller.listarTodos();
            medicamentoSelecionado = todos.stream()
                    .filter(m -> m.getId() == id)
                    .findFirst().orElse(null);
        } catch (Exception ex) {
            return;
        }
        if (medicamentoSelecionado == null) return;

        txtNome.setText(medicamentoSelecionado.getNome());
        txtFabricante.setText(medicamentoSelecionado.getFabricante());
        txtValidade.setText(medicamentoSelecionado.getDataValidade().format(FMT));
        txtQuantidade.setText(String.valueOf(medicamentoSelecionado.getQuantidade()));
        txtDescricao.setText(medicamentoSelecionado.getDescricao() != null ? medicamentoSelecionado.getDescricao() : "");
        aplicarEstado(Estado.VIEW);
    }

    private void salvar() {
        if (!validarCampos()) return;

        if (estado == Estado.EDIT && medicamentoSelecionado != null) {
            editar();
            return;
        }

        try {
            Medicamento med = new Medicamento();
            med.setNome(txtNome.getText().trim());
            med.setFabricante(txtFabricante.getText().trim());
            med.setDataValidade(LocalDate.parse(txtValidade.getText().trim(), FMT));
            med.setQuantidade(Integer.parseInt(txtQuantidade.getText().trim()));
            med.setDescricao(txtDescricao.getText().trim());

            controller.cadastrarMedicamento(med);
            JOptionPane.showMessageDialog(this, "Medicamento cadastrado com sucesso!");
            limparFormulario();
            carregarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (medicamentoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um medicamento na tabela.");
            return;
        }
        if (!validarCampos()) return;
        try {
            medicamentoSelecionado.setNome(txtNome.getText().trim());
            medicamentoSelecionado.setFabricante(txtFabricante.getText().trim());
            medicamentoSelecionado.setDataValidade(LocalDate.parse(txtValidade.getText().trim(), FMT));
            medicamentoSelecionado.setQuantidade(Integer.parseInt(txtQuantidade.getText().trim()));
            medicamentoSelecionado.setDescricao(txtDescricao.getText().trim());

            controller.atualizarMedicamento(medicamentoSelecionado);
            JOptionPane.showMessageDialog(this, "Medicamento atualizado com sucesso!");
            carregarTabela();
            aplicarEstado(Estado.VIEW);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty() || txtFabricante.getText().trim().isEmpty()
                || txtValidade.getText().trim().isEmpty() || txtQuantidade.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
            return false;
        }
        try {
            LocalDate.parse(txtValidade.getText().trim(), FMT);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data de validade inválida. Use dd/MM/yyyy.");
            return false;
        }
        try {
            Integer.parseInt(txtQuantidade.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade deve ser um número inteiro.");
            return false;
        }
        return true;
    }

    private void limparFormulario() {
        if (estado == Estado.EDIT && medicamentoSelecionado != null) {
            txtNome.setText(medicamentoSelecionado.getNome());
            txtFabricante.setText(medicamentoSelecionado.getFabricante());
            txtValidade.setText(medicamentoSelecionado.getDataValidade().format(FMT));
            txtQuantidade.setText(String.valueOf(medicamentoSelecionado.getQuantidade()));
            txtDescricao.setText(medicamentoSelecionado.getDescricao() != null ? medicamentoSelecionado.getDescricao() : "");
            aplicarEstado(Estado.VIEW);
            return;
        }
        txtNome.setText("");
        txtFabricante.setText("");
        txtValidade.setText("");
        txtQuantidade.setText("");
        txtDescricao.setText("");
        medicamentoSelecionado = null;
        tabela.clearSelection();
        aplicarEstado(Estado.NOVO);
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
}
