package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.model.Quarto;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.ResidenteRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link Residente}.
 * Realiza operações de persistência na tabela {@code residente},
 * com JOINs em {@code pessoa} e {@code quarto}.
 */
public class ResidenteRepositoryImpl implements ResidenteRepository {

    @Override
    public void salvar(Residente residente) {
        String sql = "INSERT INTO residente (id_quarto, id_pessoa, status, obs_geral) VALUES (?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, residente.getQuarto().getId());
            st.setInt(2, residente.getPessoa().getId());
            st.setString(3, residente.getStatus());
            st.setString(4, residente.getObsGeral());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                residente.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar residente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Residente residente) {
        String sql = "UPDATE residente SET id_quarto = ?, status = ?, obs_geral = ? WHERE id_residente = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, residente.getQuarto().getId());
            st.setString(2, residente.getStatus());
            st.setString(3, residente.getObsGeral());
            st.setInt(4, residente.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar residente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Residente buscarPorId(int id) {
        String sql = "SELECT r.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "q.numero, q.status AS quarto_status "
                + "FROM residente r "
                + "JOIN pessoa p ON r.id_pessoa = p.id_pessoa "
                + "JOIN quarto q ON r.id_quarto = q.id_quarto "
                + "WHERE r.id_residente = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarResidente(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar residente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public Residente buscarPorCpfPessoa(String cpf) {
        String sql = "SELECT r.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "q.numero, q.status AS quarto_status "
                + "FROM residente r "
                + "JOIN pessoa p ON r.id_pessoa = p.id_pessoa "
                + "JOIN quarto q ON r.id_quarto = q.id_quarto "
                + "WHERE p.cpf = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, cpf);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarResidente(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar residente por CPF: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public List<Residente> buscarPorNomePessoa(String nome) {
        String sql = "SELECT r.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "q.numero, q.status AS quarto_status "
                + "FROM residente r "
                + "JOIN pessoa p ON r.id_pessoa = p.id_pessoa "
                + "JOIN quarto q ON r.id_quarto = q.id_quarto "
                + "WHERE p.nome_completo ILIKE ?";
        List<Residente> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, "%" + nome + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarResidente(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar residentes por nome: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    @Override
    public List<Residente> listarTodos() {
        String sql = "SELECT r.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "q.numero, q.status AS quarto_status "
                + "FROM residente r "
                + "JOIN pessoa p ON r.id_pessoa = p.id_pessoa "
                + "JOIN quarto q ON r.id_quarto = q.id_quarto "
                + "ORDER BY p.nome_completo";
        List<Residente> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarResidente(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar residentes: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private Residente montarResidente(ResultSet rs) throws SQLException {
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
        r.setStatus(rs.getString("status"));
        r.setObsGeral(rs.getString("obs_geral"));
        return r;
    }
}
