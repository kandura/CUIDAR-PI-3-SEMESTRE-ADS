package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.model.Prontuario;
import br.com.cuidar.model.Quarto;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.ProntuarioRepository;

import java.sql.*;

/**
 * Implementação JDBC do repositório de {@link Prontuario}.
 * Realiza operações de persistência na tabela {@code prontuario},
 * com JOINs em {@code residente}, {@code pessoa} e {@code quarto}.
 */
public class ProntuarioRepositoryImpl implements ProntuarioRepository {

    @Override
    public void salvar(Prontuario prontuario) {
        String sql = "INSERT INTO prontuario (id_residente, peso, altura, tipo_sanguineo, alergias, obs_geral) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, prontuario.getResidente().getId());
            st.setDouble(2, prontuario.getPeso());
            st.setDouble(3, prontuario.getAltura());
            st.setString(4, prontuario.getTipoSanguineo());
            st.setString(5, prontuario.getAlergias());
            st.setString(6, prontuario.getObsGeral());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                prontuario.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar prontuário: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Prontuario prontuario) {
        String sql = "UPDATE prontuario SET peso = ?, altura = ?, tipo_sanguineo = ?, "
                + "alergias = ?, obs_geral = ? WHERE id_prontuario = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setDouble(1, prontuario.getPeso());
            st.setDouble(2, prontuario.getAltura());
            st.setString(3, prontuario.getTipoSanguineo());
            st.setString(4, prontuario.getAlergias());
            st.setString(5, prontuario.getObsGeral());
            st.setInt(6, prontuario.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar prontuário: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Prontuario buscarPorId(int id) {
        String sql = "SELECT pron.id_prontuario, pron.peso, pron.altura, pron.tipo_sanguineo, "
                + "pron.alergias, pron.obs_geral AS pron_obs, "
                + "r.id_residente, r.status AS res_status, r.obs_geral AS res_obs, "
                + "p.id_pessoa, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "q.id_quarto, q.numero, q.status AS quarto_status "
                + "FROM prontuario pron "
                + "JOIN residente r ON pron.id_residente = r.id_residente "
                + "JOIN pessoa p ON r.id_pessoa = p.id_pessoa "
                + "JOIN quarto q ON r.id_quarto = q.id_quarto "
                + "WHERE pron.id_prontuario = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarProntuario(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar prontuário: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public Prontuario buscarPorResidente(Residente residente) {
        String sql = "SELECT pron.id_prontuario, pron.peso, pron.altura, pron.tipo_sanguineo, "
                + "pron.alergias, pron.obs_geral AS pron_obs, "
                + "r.id_residente, r.status AS res_status, r.obs_geral AS res_obs, "
                + "p.id_pessoa, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "q.id_quarto, q.numero, q.status AS quarto_status "
                + "FROM prontuario pron "
                + "JOIN residente r ON pron.id_residente = r.id_residente "
                + "JOIN pessoa p ON r.id_pessoa = p.id_pessoa "
                + "JOIN quarto q ON r.id_quarto = q.id_quarto "
                + "WHERE pron.id_residente = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, residente.getId());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarProntuario(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar prontuário por residente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    private Prontuario montarProntuario(ResultSet rs) throws SQLException {
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

        Quarto q = new Quarto();
        q.setId(rs.getInt("id_quarto"));
        q.setNumero(rs.getInt("numero"));
        q.setStatus(rs.getString("quarto_status"));

        Residente r = new Residente();
        r.setId(rs.getInt("id_residente"));
        r.setPessoa(p);
        r.setQuarto(q);
        r.setStatus(rs.getString("res_status"));
        r.setObsGeral(rs.getString("res_obs"));

        Prontuario pron = new Prontuario();
        pron.setId(rs.getInt("id_prontuario"));
        pron.setResidente(r);
        pron.setPeso(rs.getDouble("peso"));
        pron.setAltura(rs.getDouble("altura"));
        pron.setTipoSanguineo(rs.getString("tipo_sanguineo"));
        pron.setAlergias(rs.getString("alergias"));
        pron.setObsGeral(rs.getString("pron_obs"));
        return pron;
    }
}
