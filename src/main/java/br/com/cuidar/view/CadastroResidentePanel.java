package br.com.cuidar.view;

import br.com.cuidar.controller.ResidenteController;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.model.Quarto;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.QuartoRepository;
import br.com.cuidar.util.CpfUtil;
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

/**
 * Painel de cadastro e edição de {@link Residente}.
 * Exibe um formulário com campos de dados pessoais e quarto,
 * além de uma {@link JTable} com filtro em tempo real por nome.
 * Segue o padrão do professor: formulário + tabela no mesmo painel,
 * clique na tabela preenche o formulário.
 */
public class CadastroResidentePanel extends JPanel {

    private enum Estado { NOVO, VIEW, EDIT }

    private final ResidenteController controller;
    private final QuartoRepository quartoRepository;

    private JTextField txtNome, txtCpf, txtDataNasc, txtFiltro;
    private JComboBox<String> cmbSexo, cmbStatus;
    private JComboBox<Quarto> cmbQuarto;
    private JTextArea txtObs;
    private JTable tabela;
    private DefaultTableModel tableModel;
    private Residente residenteSelecionado;

    private JButton btnSalvar, btnEditar, btnLimpar;
    private Estado estado = Estado.NOVO;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CadastroResidentePanel(ResidenteController controller, QuartoRepository quartoRepository) {
        this.controller = controller;
        this.quartoRepository = quartoRepository;
        setLayout(new BorderLayout(0, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        initComponents();
        carregarTabela();
    }

    private void initComponents() {
        // ===== TITULO =====
        JLabel lblTitulo = new JLabel("Cadastro de Residentes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 102, 153));

        // ===== FORMULARIO (NORTH) =====
        JPanel topo = new JPanel(new BorderLayout(0, 10));
        topo.setBackground(Color.WHITE);
        topo.add(lblTitulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(3, 5, 3, 5);
        g.weightx = 1.0;

        // Linha 1: Nome | CPF | Data Nasc.
        addLabel(form, g, 0, 0, "Nome Completo:");
        addLabel(form, g, 1, 0, "CPF:");
        addLabel(form, g, 2, 0, "Data Nasc. (dd/MM/yyyy):");

        txtNome = new JTextField();
        txtCpf = new JTextField();
        txtDataNasc = new JTextField();
        InputHelper.aplicarMascaraCpf(txtCpf);
        InputHelper.aplicarMascaraData(txtDataNasc);
        addField(form, g, 0, 1, txtNome);
        addField(form, g, 1, 1, txtCpf);
        addField(form, g, 2, 1, txtDataNasc);

        // Linha 2: Sexo | Quarto | Status
        addLabel(form, g, 0, 2, "Sexo:");
        addLabel(form, g, 1, 2, "Quarto:");
        addLabel(form, g, 2, 2, "Status:");

        cmbSexo = new JComboBox<>(new String[]{"Masculino", "Feminino"});
        cmbQuarto = new JComboBox<>();
        cmbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Falecido"});
        carregarQuartos();
        addField(form, g, 0, 3, cmbSexo);
        addField(form, g, 1, 3, cmbQuarto);
        addField(form, g, 2, 3, cmbStatus);

        // Linha 3: Observações (span 3 cols)
        addLabel(form, g, 0, 4, "Observações:");
        txtObs = new JTextArea(3, 20);
        txtObs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtObs.setLineWrap(true);
        txtObs.setWrapStyleWord(true);
        JScrollPane scrollObs = new JScrollPane(txtObs);
        g.gridx = 0; g.gridy = 5; g.gridwidth = 3; g.fill = GridBagConstraints.BOTH; g.weighty = 0.3;
        form.add(scrollObs, g);
        g.gridwidth = 1; g.weighty = 0; g.fill = GridBagConstraints.HORIZONTAL;

        // Linha 4: Botões
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

        g.gridx = 0; g.gridy = 6; g.gridwidth = 3; g.anchor = GridBagConstraints.WEST;
        form.add(botoes, g);
        g.gridwidth = 1;

        topo.add(form, BorderLayout.CENTER);

        // ===== FILTRO + TABELA (CENTER) =====
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

        String[] colunas = {"ID", "Nome", "CPF", "Sexo", "Data Nasc.", "Quarto", "Status"};
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
        // CPF: liberável só em NOVO (em EDIT permanece bloqueado)
        txtCpf.setEditable(novo == Estado.NOVO);
        txtDataNasc.setEditable(editavel);
        txtObs.setEditable(editavel);
        cmbSexo.setEnabled(editavel);
        cmbQuarto.setEnabled(editavel);
        cmbStatus.setEnabled(editavel);

        switch (novo) {
            case NOVO:
                btnSalvar.setText("Salvar");
                btnSalvar.setVisible(true);
                btnEditar.setVisible(false);
                btnLimpar.setText("Limpar");
                btnLimpar.setVisible(true);
                break;
            case VIEW:
                btnSalvar.setVisible(false);
                btnEditar.setText("Editar");
                btnEditar.setVisible(true);
                btnLimpar.setText("Limpar");
                btnLimpar.setVisible(true);
                break;
            case EDIT:
                btnSalvar.setText("Salvar");
                btnSalvar.setVisible(true);
                btnEditar.setVisible(false);
                btnLimpar.setText("Cancelar");
                btnLimpar.setVisible(true);
                break;
        }
    }

    private void entrarModoEdicao() {
        if (residenteSelecionado == null) return;
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

    private void carregarQuartos() {
        cmbQuarto.removeAllItems();
        try {
            List<Quarto> quartos = quartoRepository.listarTodos();
            for (Quarto q : quartos) {
                if ("Disponível".equalsIgnoreCase(q.getStatus())) {
                    cmbQuarto.addItem(q);
                }
            }
        } catch (Exception ex) {
            // Sem conexão — combo fica vazio
        }
    }

    private void carregarQuartosComAtual(Quarto quartoAtual) {
        cmbQuarto.removeAllItems();
        try {
            List<Quarto> quartos = quartoRepository.listarTodos();
            boolean atualAdicionado = false;
            for (Quarto q : quartos) {
                if ("Disponível".equalsIgnoreCase(q.getStatus())) {
                    cmbQuarto.addItem(q);
                } else if (quartoAtual != null && q.getId() == quartoAtual.getId()) {
                    cmbQuarto.addItem(q);
                    atualAdicionado = true;
                }
            }
        } catch (Exception ex) {
            // Sem conexão — combo fica vazio
        }
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Residente> lista = controller.listarTodos();
            for (Residente r : lista) {
                tableModel.addRow(new Object[]{
                        r.getId(),
                        r.getPessoa().getNomeCompleto(),
                        r.getPessoa().getCpf(),
                        r.getPessoa().getSexo(),
                        r.getPessoa().getDataNascimento().format(FMT),
                        r.getQuarto().getNumero(),
                        r.getStatus()
                });
            }
        } catch (Exception ex) {
            // Sem conexão — tabela fica vazia
        }
    }

    private void filtrar() {
        String filtro = txtFiltro.getText().trim();
        tableModel.setRowCount(0);
        try {
            List<Residente> lista = filtro.isEmpty()
                    ? controller.listarTodos()
                    : controller.buscarPorNome(filtro);
            for (Residente r : lista) {
                tableModel.addRow(new Object[]{
                        r.getId(),
                        r.getPessoa().getNomeCompleto(),
                        r.getPessoa().getCpf(),
                        r.getPessoa().getSexo(),
                        r.getPessoa().getDataNascimento().format(FMT),
                        r.getQuarto().getNumero(),
                        r.getStatus()
                });
            }
        } catch (Exception ex) {
            // ignore
        }
    }

    private void preencherFormulario() {
        int row = tabela.getSelectedRow();
        if (row < 0) return;

        int id = (int) tableModel.getValueAt(row, 0);
        try {
            List<Residente> todos = controller.listarTodos();
            residenteSelecionado = todos.stream()
                    .filter(r -> r.getId() == id)
                    .findFirst().orElse(null);
        } catch (Exception ex) {
            return;
        }

        if (residenteSelecionado == null) return;

        txtNome.setText(residenteSelecionado.getPessoa().getNomeCompleto());
        txtCpf.setText(residenteSelecionado.getPessoa().getCpf());
        txtDataNasc.setText(residenteSelecionado.getPessoa().getDataNascimento().format(FMT));
        cmbSexo.setSelectedItem(residenteSelecionado.getPessoa().getSexo());
        cmbStatus.setSelectedItem(residenteSelecionado.getStatus());
        txtObs.setText(residenteSelecionado.getObsGeral() != null ? residenteSelecionado.getObsGeral() : "");

        carregarQuartosComAtual(residenteSelecionado.getQuarto());
        for (int i = 0; i < cmbQuarto.getItemCount(); i++) {
            Quarto q = cmbQuarto.getItemAt(i);
            if (q.getId() == residenteSelecionado.getQuarto().getId()) {
                cmbQuarto.setSelectedIndex(i);
                break;
            }
        }
        aplicarEstado(Estado.VIEW);
    }

    private void salvar() {
        if (!validarCampos()) return;

        if (estado == Estado.EDIT && residenteSelecionado != null) {
            editar();
            return;
        }

        try {
            Pessoa pessoa = new Pessoa();
            pessoa.setNomeCompleto(txtNome.getText().trim());
            pessoa.setCpf(txtCpf.getText().trim().replaceAll("[^0-9]", ""));
            pessoa.setSexo((String) cmbSexo.getSelectedItem());
            pessoa.setDataNascimento(LocalDate.parse(txtDataNasc.getText().trim(), FMT));

            Residente residente = new Residente();
            residente.setPessoa(pessoa);
            residente.setQuarto((Quarto) cmbQuarto.getSelectedItem());
            residente.setStatus((String) cmbStatus.getSelectedItem());
            residente.setObsGeral(txtObs.getText().trim());

            controller.cadastrarResidente(residente);
            JOptionPane.showMessageDialog(this, "Residente cadastrado com sucesso!");
            limparFormulario();
            carregarTabela();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (residenteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um residente na tabela.");
            return;
        }
        if (!validarCampos()) return;

        try {
            residenteSelecionado.getPessoa().setNomeCompleto(txtNome.getText().trim());
            residenteSelecionado.getPessoa().setSexo((String) cmbSexo.getSelectedItem());
            residenteSelecionado.getPessoa().setDataNascimento(
                    LocalDate.parse(txtDataNasc.getText().trim(), FMT));
            residenteSelecionado.setQuarto((Quarto) cmbQuarto.getSelectedItem());
            residenteSelecionado.setStatus((String) cmbStatus.getSelectedItem());
            residenteSelecionado.setObsGeral(txtObs.getText().trim());

            controller.editarResidente(residenteSelecionado);
            JOptionPane.showMessageDialog(this, "Residente atualizado com sucesso!");
            carregarTabela();
            aplicarEstado(Estado.VIEW);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty() || txtCpf.getText().trim().isEmpty()
                || txtDataNasc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios (Nome, CPF, Data Nasc.).");
            return false;
        }
        if (!CpfUtil.isValid(txtCpf.getText().trim())) {
            JOptionPane.showMessageDialog(this, "CPF inválido. Verifique os dígitos.");
            return false;
        }
        try {
            LocalDate.parse(txtDataNasc.getText().trim(), FMT);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Use dd/MM/yyyy.");
            return false;
        }
        if (cmbQuarto.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um quarto.");
            return false;
        }
        return true;
    }

    private void limparFormulario() {
        if (estado == Estado.EDIT && residenteSelecionado != null) {
            // Cancelar edição: recarrega valores originais
            txtNome.setText(residenteSelecionado.getPessoa().getNomeCompleto());
            txtCpf.setText(residenteSelecionado.getPessoa().getCpf());
            txtDataNasc.setText(residenteSelecionado.getPessoa().getDataNascimento().format(FMT));
            cmbSexo.setSelectedItem(residenteSelecionado.getPessoa().getSexo());
            cmbStatus.setSelectedItem(residenteSelecionado.getStatus());
            txtObs.setText(residenteSelecionado.getObsGeral() != null ? residenteSelecionado.getObsGeral() : "");
            carregarQuartosComAtual(residenteSelecionado.getQuarto());
            for (int i = 0; i < cmbQuarto.getItemCount(); i++) {
                if (cmbQuarto.getItemAt(i).getId() == residenteSelecionado.getQuarto().getId()) {
                    cmbQuarto.setSelectedIndex(i); break;
                }
            }
            aplicarEstado(Estado.VIEW);
            return;
        }
        txtNome.setText("");
        txtCpf.setText("");
        txtDataNasc.setText("");
        cmbSexo.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        txtObs.setText("");
        residenteSelecionado = null;
        tabela.clearSelection();
        carregarQuartos();
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
