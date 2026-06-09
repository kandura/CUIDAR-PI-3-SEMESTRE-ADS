package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Medicamento;
import br.com.cuidar.repository.MedicamentoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link Medicamento}.
 * Realiza operações de persistência na tabela {@code medicamento}.
 */
public class MedicamentoRepositoryImpl implements MedicamentoRepository {

    @Override
    public void salvar(Medicamento medicamento) {
        String sql = "INSERT INTO medicamento (nome, fabricante, data_validade, quantidade, descricao) "
                + "VALUES (?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, medicamento.getNome());
            st.setString(2, medicamento.getFabricante());
            st.setDate(3, Date.valueOf(medicamento.getDataValidade()));
            st.setInt(4, medicamento.getQuantidade());
            st.setString(5, medicamento.getDescricao());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                medicamento.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar medicamento: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Medicamento medicamento) {
        String sql = "UPDATE medicamento SET nome = ?, fabricante = ?, data_validade = ?, "
                + "quantidade = ?, descricao = ? WHERE id_medicamento = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, medicamento.getNome());
            st.setString(2, medicamento.getFabricante());
            st.setDate(3, Date.valueOf(medicamento.getDataValidade()));
            st.setInt(4, medicamento.getQuantidade());
            st.setString(5, medicamento.getDescricao());
            st.setInt(6, medicamento.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar medicamento: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Medicamento buscarPorId(int id) {
        String sql = "SELECT * FROM medicamento WHERE id_medicamento = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarMedicamento(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar medicamento: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public List<Medicamento> listarTodos() {
        String sql = "SELECT * FROM medicamento ORDER BY nome";
        List<Medicamento> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarMedicamento(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar medicamentos: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private Medicamento montarMedicamento(ResultSet rs) throws SQLException {
        Medicamento m = new Medicamento();
        m.setId(rs.getInt("id_medicamento"));
        m.setNome(rs.getString("nome"));
        m.setFabricante(rs.getString("fabricante"));
        m.setDataValidade(rs.getDate("data_validade").toLocalDate());
        m.setQuantidade(rs.getInt("quantidade"));
        m.setDescricao(rs.getString("descricao"));
        return m;
    }
}
