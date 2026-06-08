package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Quarto;
import br.com.cuidar.repository.QuartoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link Quarto}.
 * Realiza operações de persistência na tabela {@code quarto}.
 */
public class QuartoRepositoryImpl implements QuartoRepository {

    @Override
    public void salvar(Quarto quarto) {
        String sql = "INSERT INTO quarto (numero, status) VALUES (?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, quarto.getNumero());
            st.setString(2, quarto.getStatus());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                quarto.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar quarto: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Quarto quarto) {
        String sql = "UPDATE quarto SET numero = ?, status = ? WHERE id_quarto = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, quarto.getNumero());
            st.setString(2, quarto.getStatus());
            st.setInt(3, quarto.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar quarto: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Quarto buscarPorId(int id) {
        String sql = "SELECT * FROM quarto WHERE id_quarto = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarQuarto(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar quarto: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public List<Quarto> listarTodos() {
        String sql = "SELECT * FROM quarto ORDER BY numero";
        List<Quarto> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarQuarto(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar quartos: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    @Override
    public List<Quarto> listarPorStatus(String status) {
        String sql = "SELECT * FROM quarto WHERE status = ? ORDER BY numero";
        List<Quarto> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, status);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarQuarto(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar quartos por status: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private Quarto montarQuarto(ResultSet rs) throws SQLException {
        Quarto q = new Quarto();
        q.setId(rs.getInt("id_quarto"));
        q.setNumero(rs.getInt("numero"));
        q.setStatus(rs.getString("status"));
        return q;
    }
}
