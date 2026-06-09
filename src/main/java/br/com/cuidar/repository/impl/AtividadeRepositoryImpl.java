package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Atividade;
import br.com.cuidar.repository.AtividadeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link Atividade}.
 * Realiza operações de persistência na tabela {@code atividade}.
 */
public class AtividadeRepositoryImpl implements AtividadeRepository {

    @Override
    public void salvar(Atividade atividade) {
        String sql = "INSERT INTO atividade (nome, descricao, dia_semana, hora_inicio, hora_termino) "
                + "VALUES (?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, atividade.getNome());
            st.setString(2, atividade.getDescricao());
            st.setString(3, atividade.getDiaSemana());
            st.setTime(4, Time.valueOf(atividade.getHoraInicio()));
            st.setTime(5, Time.valueOf(atividade.getHoraTermino()));
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                atividade.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar atividade: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Atividade atividade) {
        String sql = "UPDATE atividade SET nome = ?, descricao = ?, dia_semana = ?, "
                + "hora_inicio = ?, hora_termino = ? WHERE id_atividade = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, atividade.getNome());
            st.setString(2, atividade.getDescricao());
            st.setString(3, atividade.getDiaSemana());
            st.setTime(4, Time.valueOf(atividade.getHoraInicio()));
            st.setTime(5, Time.valueOf(atividade.getHoraTermino()));
            st.setInt(6, atividade.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar atividade: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM atividade WHERE id_atividade = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir atividade: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Atividade buscarPorId(int id) {
        String sql = "SELECT * FROM atividade WHERE id_atividade = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarAtividade(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar atividade: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public List<Atividade> listarTodos() {
        String sql = "SELECT * FROM atividade ORDER BY dia_semana, hora_inicio";
        List<Atividade> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarAtividade(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar atividades: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    @Override
    public List<Atividade> listarPorDiaSemana(String diaSemana) {
        String sql = "SELECT * FROM atividade WHERE dia_semana = ? ORDER BY hora_inicio";
        List<Atividade> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, diaSemana);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarAtividade(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar atividades por dia: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private Atividade montarAtividade(ResultSet rs) throws SQLException {
        Atividade a = new Atividade();
        a.setId(rs.getInt("id_atividade"));
        a.setNome(rs.getString("nome"));
        a.setDescricao(rs.getString("descricao"));
        a.setDiaSemana(rs.getString("dia_semana"));
        a.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
        a.setHoraTermino(rs.getTime("hora_termino").toLocalTime());
        return a;
    }
}
