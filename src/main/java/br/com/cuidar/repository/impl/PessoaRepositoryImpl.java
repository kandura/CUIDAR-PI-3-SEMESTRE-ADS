package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.repository.PessoaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link Pessoa}.
 * Realiza operações de persistência na tabela {@code pessoa}.
 */
public class PessoaRepositoryImpl implements PessoaRepository {

    @Override
    public void salvar(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (nome_completo, cpf, sexo, data_nascimento) VALUES (?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, pessoa.getNomeCompleto());
            st.setString(2, pessoa.getCpf());
            st.setString(3, pessoa.getSexo());
            st.setDate(4, Date.valueOf(pessoa.getDataNascimento()));
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                pessoa.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar pessoa: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Pessoa pessoa) {
        String sql = "UPDATE pessoa SET nome_completo = ?, cpf = ?, sexo = ?, data_nascimento = ? WHERE id_pessoa = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, pessoa.getNomeCompleto());
            st.setString(2, pessoa.getCpf());
            st.setString(3, pessoa.getSexo());
            st.setDate(4, Date.valueOf(pessoa.getDataNascimento()));
            st.setInt(5, pessoa.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pessoa: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Pessoa buscarPorId(int id) {
        String sql = "SELECT * FROM pessoa WHERE id_pessoa = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarPessoa(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoa: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public Pessoa buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM pessoa WHERE cpf = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, cpf);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarPessoa(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pessoa por CPF: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public List<Pessoa> listarTodos() {
        String sql = "SELECT * FROM pessoa ORDER BY nome_completo";
        List<Pessoa> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarPessoa(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pessoas: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private Pessoa montarPessoa(ResultSet rs) throws SQLException {
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
        return p;
    }
}
