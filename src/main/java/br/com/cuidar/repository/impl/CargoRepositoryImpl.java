package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Cargo;
import br.com.cuidar.repository.CargoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link Cargo}.
 * Realiza operações de persistência na tabela {@code cargo}.
 */
public class CargoRepositoryImpl implements CargoRepository {

    @Override
    public void salvar(Cargo cargo) {
        String sql = "INSERT INTO cargo (nome_cargo, descricao) VALUES (?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, cargo.getNomeCargo());
            st.setString(2, cargo.getDescricao());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                cargo.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cargo: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Cargo cargo) {
        String sql = "UPDATE cargo SET nome_cargo = ?, descricao = ? WHERE id_cargo = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, cargo.getNomeCargo());
            st.setString(2, cargo.getDescricao());
            st.setInt(3, cargo.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cargo: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Cargo buscarPorId(int id) {
        String sql = "SELECT * FROM cargo WHERE id_cargo = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarCargo(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cargo: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public List<Cargo> listarTodos() {
        String sql = "SELECT * FROM cargo ORDER BY nome_cargo";
        List<Cargo> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarCargo(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar cargos: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private Cargo montarCargo(ResultSet rs) throws SQLException {
        Cargo c = new Cargo();
        c.setId(rs.getInt("id_cargo"));
        c.setNomeCargo(rs.getString("nome_cargo"));
        c.setDescricao(rs.getString("descricao"));
        return c;
    }
}
