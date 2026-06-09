package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.model.Responsavel;
import br.com.cuidar.repository.ResponsavelRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link Responsavel}.
 * Realiza operações de persistência na tabela {@code responsavel},
 * com JOIN em {@code pessoa}.
 */
public class ResponsavelRepositoryImpl implements ResponsavelRepository {

    @Override
    public void salvar(Responsavel responsavel) {
        String sql = "INSERT INTO responsavel (id_pessoa, telefone, email, rua, numero, bairro, cidade, estado, cep) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, responsavel.getPessoa().getId());
            st.setString(2, responsavel.getTelefone());
            st.setString(3, responsavel.getEmail());
            st.setString(4, responsavel.getRua());
            st.setInt(5, responsavel.getNumero());
            st.setString(6, responsavel.getBairro());
            st.setString(7, responsavel.getCidade());
            st.setString(8, responsavel.getEstado());
            st.setString(9, responsavel.getCep());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                responsavel.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar responsável: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Responsavel responsavel) {
        String sql = "UPDATE responsavel SET telefone = ?, email = ?, rua = ?, numero = ?, "
                + "bairro = ?, cidade = ?, estado = ?, cep = ? WHERE id_responsavel = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, responsavel.getTelefone());
            st.setString(2, responsavel.getEmail());
            st.setString(3, responsavel.getRua());
            st.setInt(4, responsavel.getNumero());
            st.setString(5, responsavel.getBairro());
            st.setString(6, responsavel.getCidade());
            st.setString(7, responsavel.getEstado());
            st.setString(8, responsavel.getCep());
            st.setInt(9, responsavel.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar responsável: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Responsavel buscarPorId(int id) {
        String sql = "SELECT resp.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro "
                + "FROM responsavel resp "
                + "JOIN pessoa p ON resp.id_pessoa = p.id_pessoa "
                + "WHERE resp.id_responsavel = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarResponsavel(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar responsável: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public List<Responsavel> listarTodos() {
        String sql = "SELECT resp.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro "
                + "FROM responsavel resp "
                + "JOIN pessoa p ON resp.id_pessoa = p.id_pessoa "
                + "ORDER BY p.nome_completo";
        List<Responsavel> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarResponsavel(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar responsáveis: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private Responsavel montarResponsavel(ResultSet rs) throws SQLException {
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

        Responsavel resp = new Responsavel();
        resp.setId(rs.getInt("id_responsavel"));
        resp.setPessoa(p);
        resp.setTelefone(rs.getString("telefone"));
        resp.setEmail(rs.getString("email"));
        resp.setRua(rs.getString("rua"));
        resp.setNumero(rs.getInt("numero"));
        resp.setBairro(rs.getString("bairro"));
        resp.setCidade(rs.getString("cidade"));
        resp.setEstado(rs.getString("estado"));
        resp.setCep(rs.getString("cep"));
        return resp;
    }
}
