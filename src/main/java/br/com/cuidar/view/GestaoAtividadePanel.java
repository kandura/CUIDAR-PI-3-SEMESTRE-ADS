package br.com.cuidar.view;

import br.com.cuidar.controller.AtividadeController;
import br.com.cuidar.model.Atividade;
import br.com.cuidar.util.InputHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Painel de gestão de {@link Atividade} da programação semanal da ILPI.
 * Permite cadastrar, editar, excluir e listar atividades.
 * Oferece filtro por dia da semana.
 */
public class GestaoAtividadePanel extends JPanel {

    private enum Estado { NOVO, VIEW, EDIT }

    private final AtividadeController controller;

    private JTextField txtNome, txtHoraInicio, txtHoraTermino;
    private JComboBox<String> cmbDiaSemana;
    private JTextArea txtDescricao;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private Atividade atividadeSelecionada;

    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private Estado estado = Estado.NOVO;

    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final String[] DIAS = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"};

    public GestaoAtividadePanel(AtividadeController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Gestão de Atividades");
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

        // Linha 1: Nome | Dia da Semana | Hora Início | Hora Término
        addLabel(form, g, 0, 0, "Nome:");
        addLabel(form, g, 1, 0, "Dia da Semana:");
        addLabel(form, g, 2, 0, "Hora Início (HH:mm):");
        addLabel(form, g, 3, 0, "Hora Término (HH:mm):");

        txtNome = new JTextField();
        cmbDiaSemana = new JComboBox<>(DIAS);
        txtHoraInicio = new JTextField();
        txtHoraTermino = new JTextField();
        InputHelper.aplicarMascaraHora(txtHoraInicio);
        InputHelper.aplicarMascaraHora(txtHoraTermino);

        addField(form, g, 0, 1, txtNome);
        addField(form, g, 1, 1, cmbDiaSemana);
        addField(form, g, 2, 1, txtHoraInicio);
        addField(form, g, 3, 1, txtHoraTermino);

        // Linha 2: Descrição (span 4)
        addLabel(form, g, 0, 2, "Descrição:");
        txtDescricao = new JTextArea(3, 20);
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 4; g.fill = GridBagConstraints.BOTH; g.weighty = 0.3;
        form.add(scrollDesc, g);
        g.gridwidth = 1; g.weighty = 0; g.fill = GridBagConstraints.HORIZONTAL;

        // Botões
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botoes.setBackground(Color.WHITE);
        btnSalvar = criarBotao("Salvar", new Color(0, 153, 76));
        btnSalvar.addActionListener(e -> salvar());
        btnEditar = criarBotao("Editar", new Color(0, 102, 153));
        btnEditar.addActionListener(e -> entrarModoEdicao());
        btnExcluir = criarBotao("Excluir", new Color(180, 50, 50));
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar = criarBotao("Limpar", new Color(150, 150, 150));
        btnLimpar.addActionListener(e -> limparFormulario());
        botoes.add(btnSalvar);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 4; g.anchor = GridBagConstraints.WEST;
        form.add(botoes, g);
        g.gridwidth = 1;

        topo.add(form, BorderLayout.CENTER);

        // Centro: filtro + tabela
        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setBackground(Color.WHITE);

        JPanel filtroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        filtroPanel.setBackground(Color.WHITE);
        JLabel lblFiltro = new JLabel("Filtrar por dia:");
        lblFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JComboBox<String> cmbFiltroDia = new JComboBox<>();
        cmbFiltroDia.addItem("Todos");
        for (String d : DIAS) cmbFiltroDia.addItem(d);
        cmbFiltroDia.addActionListener(e -> {
            String dia = (String) cmbFiltroDia.getSelectedItem();
            if ("Todos".equals(dia)) {
                carregarTabela();
            } else {
                filtrarPorDia(dia);
            }
        });
        filtroPanel.add(lblFiltro);
        filtroPanel.add(cmbFiltroDia);

        String[] colunas = {"ID", "Nome", "Dia", "Início", "Término", "Descrição"};
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
        txtHoraInicio.setEditable(editavel);
        txtHoraTermino.setEditable(editavel);
        txtDescricao.setEditable(editavel);
        cmbDiaSemana.setEnabled(editavel);

        switch (novo) {
            case NOVO:
                btnSalvar.setText("Salvar");
                btnSalvar.setVisible(true);
                btnEditar.setVisible(false);
                btnExcluir.setVisible(false);
                btnLimpar.setVisible(true);
                btnLimpar.setText("Limpar");
                break;
            case VIEW:
                btnSalvar.setVisible(false);
                btnEditar.setText("Editar");
                btnEditar.setVisible(true);
                btnExcluir.setVisible(true);
                btnLimpar.setVisible(true);
                btnLimpar.setText("Limpar");
                break;
            case EDIT:
                btnSalvar.setText("Salvar");
                btnSalvar.setVisible(true);
                btnEditar.setVisible(false);
                btnExcluir.setVisible(false);
                btnLimpar.setVisible(true);
                btnLimpar.setText("Cancelar");
                break;
        }
    }

    private void entrarModoEdicao() {
        if (atividadeSelecionada == null) return;
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
            popularTabela(controller.listarTodos());
        } catch (Exception ex) {
            // sem conexão
        }
    }

    private void filtrarPorDia(String dia) {
        tableModel.setRowCount(0);
        try {
            popularTabela(controller.listarPorDia(dia));
        } catch (Exception ex) {
            // ignore
        }
    }

    private void popularTabela(List<Atividade> lista) {
        tableModel.setRowCount(0);
        for (Atividade a : lista) {
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getNome(),
                    a.getDiaSemana(),
                    a.getHoraInicio().format(FMT_HORA),
                    a.getHoraTermino().format(FMT_HORA),
                    a.getDescricao()
            });
        }
    }

    private void preencherFormulario() {
        int row = tabela.getSelectedRow();
        if (row < 0) return;

        int id = (int) tableModel.getValueAt(row, 0);
        try {
            List<Atividade> todos = controller.listarTodos();
            atividadeSelecionada = todos.stream()
                    .filter(a -> a.getId() == id)
                    .findFirst().orElse(null);
        } catch (Exception ex) {
            return;
        }
        if (atividadeSelecionada == null) return;

        txtNome.setText(atividadeSelecionada.getNome());
        cmbDiaSemana.setSelectedItem(atividadeSelecionada.getDiaSemana());
        txtHoraInicio.setText(atividadeSelecionada.getHoraInicio().format(FMT_HORA));
        txtHoraTermino.setText(atividadeSelecionada.getHoraTermino().format(FMT_HORA));
        txtDescricao.setText(atividadeSelecionada.getDescricao() != null ? atividadeSelecionada.getDescricao() : "");
        aplicarEstado(Estado.VIEW);
    }

    private void salvar() {
        if (!validarCampos()) return;

        if (estado == Estado.EDIT && atividadeSelecionada != null) {
            editar();
            return;
        }

        try {
            Atividade a = new Atividade();
            a.setNome(txtNome.getText().trim());
            a.setDiaSemana((String) cmbDiaSemana.getSelectedItem());
            a.setHoraInicio(LocalTime.parse(txtHoraInicio.getText().trim(), FMT_HORA));
            a.setHoraTermino(LocalTime.parse(txtHoraTermino.getText().trim(), FMT_HORA));
            a.setDescricao(txtDescricao.getText().trim());

            controller.cadastrarAtividade(a);
            JOptionPane.showMessageDialog(this, "Atividade cadastrada com sucesso!");
            limparFormulario();
            carregarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (atividadeSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma atividade na tabela.");
            return;
        }
        if (!validarCampos()) return;
        try {
            atividadeSelecionada.setNome(txtNome.getText().trim());
            atividadeSelecionada.setDiaSemana((String) cmbDiaSemana.getSelectedItem());
            atividadeSelecionada.setHoraInicio(LocalTime.parse(txtHoraInicio.getText().trim(), FMT_HORA));
            atividadeSelecionada.setHoraTermino(LocalTime.parse(txtHoraTermino.getText().trim(), FMT_HORA));
            atividadeSelecionada.setDescricao(txtDescricao.getText().trim());

            controller.atualizarAtividade(atividadeSelecionada);
            JOptionPane.showMessageDialog(this, "Atividade atualizada com sucesso!");
            carregarTabela();
            aplicarEstado(Estado.VIEW);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        if (atividadeSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma atividade na tabela.");
            return;
        }
        int op = JOptionPane.showConfirmDialog(this, "Deseja excluir a atividade \"" +
                atividadeSelecionada.getNome() + "\"?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;
        try {
            controller.excluirAtividade(atividadeSelecionada.getId());
            JOptionPane.showMessageDialog(this, "Atividade excluída!");
            limparFormulario();
            carregarTabela();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty() || txtHoraInicio.getText().trim().isEmpty()
                || txtHoraTermino.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha Nome, Hora Início e Hora Término.");
            return false;
        }
        try {
            LocalTime.parse(txtHoraInicio.getText().trim(), FMT_HORA);
            LocalTime.parse(txtHoraTermino.getText().trim(), FMT_HORA);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Horário inválido. Use HH:mm.");
            return false;
        }
        return true;
    }

    private void limparFormulario() {
        if (estado == Estado.EDIT && atividadeSelecionada != null) {
            // Cancelar edição: recarrega valores originais e volta ao VIEW
            txtNome.setText(atividadeSelecionada.getNome());
            cmbDiaSemana.setSelectedItem(atividadeSelecionada.getDiaSemana());
            txtHoraInicio.setText(atividadeSelecionada.getHoraInicio().format(FMT_HORA));
            txtHoraTermino.setText(atividadeSelecionada.getHoraTermino().format(FMT_HORA));
            txtDescricao.setText(atividadeSelecionada.getDescricao() != null ? atividadeSelecionada.getDescricao() : "");
            aplicarEstado(Estado.VIEW);
            return;
        }
        txtNome.setText("");
        txtHoraInicio.setText("");
        txtHoraTermino.setText("");
        txtDescricao.setText("");
        cmbDiaSemana.setSelectedIndex(0);
        atividadeSelecionada = null;
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
