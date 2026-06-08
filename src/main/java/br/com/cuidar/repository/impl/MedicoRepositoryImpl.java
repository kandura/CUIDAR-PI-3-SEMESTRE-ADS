package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Medico;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.repository.MedicoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link Medico}.
 * Realiza operações de persistência na tabela {@code medico},
 * com JOIN em {@code pessoa}.
 */
public class MedicoRepositoryImpl implements MedicoRepository {

    @Override
    public void salvar(Medico medico) {
        String sql = "INSERT INTO medico (id_pessoa, crm, especialidade, telefone, email) VALUES (?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, medico.getPessoa().getId());
            st.setString(2, medico.getCrm());
            st.setString(3, medico.getEspecialidade());
            st.setString(4, medico.getTelefone());
            st.setString(5, medico.getEmail());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                medico.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar médico: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Medico medico) {
        String sql = "UPDATE medico SET crm = ?, especialidade = ?, telefone = ?, email = ? WHERE id_medico = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, medico.getCrm());
            st.setString(2, medico.getEspecialidade());
            st.setString(3, medico.getTelefone());
            st.setString(4, medico.getEmail());
            st.setInt(5, medico.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar médico: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Medico buscarPorId(int id) {
        String sql = "SELECT med.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro "
                + "FROM medico med "
                + "JOIN pessoa p ON med.id_pessoa = p.id_pessoa "
                + "WHERE med.id_medico = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarMedico(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar médico: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public Medico buscarPorCrm(String crm) {
        String sql = "SELECT med.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro "
                + "FROM medico med "
                + "JOIN pessoa p ON med.id_pessoa = p.id_pessoa "
                + "WHERE med.crm = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, crm);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarMedico(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar médico por CRM: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public List<Medico> listarTodos() {
        String sql = "SELECT med.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro "
                + "FROM medico med "
                + "JOIN pessoa p ON med.id_pessoa = p.id_pessoa "
                + "ORDER BY p.nome_completo";
        List<Medico> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarMedico(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar médicos: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private Medico montarMedico(ResultSet rs) throws SQLException {
        Pessoa p = new Pessoa();
        p.setId(rs.getInt("id_pessoa"));
        p.setNomeCompleto(rs.getString("nome_completo"));
        p.setCpf(rs.getString("cpf"));
        p.setSexo(rs.getString("sexo"));
        p.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        Timestamp ts = rs.getTimestamp("data_cadastro");
        if (ts != null) {
            p.setDataCadastro(ts.toLocalDateTime());
        }

        Medico med = new Medico();
        med.setId(rs.getInt("id_medico"));
        med.setPessoa(p);
        med.setCrm(rs.getString("crm"));
        med.setEspecialidade(rs.getString("especialidade"));
        med.setTelefone(rs.getString("telefone"));
        med.setEmail(rs.getString("email"));
        return med;
    }
}
