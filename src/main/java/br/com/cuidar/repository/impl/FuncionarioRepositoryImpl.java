package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Cargo;
import br.com.cuidar.model.Funcionario;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.repository.FuncionarioRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link Funcionario}.
 * Realiza operações de persistência na tabela {@code funcionario},
 * com JOINs em {@code pessoa} e {@code cargo}.
 */
public class FuncionarioRepositoryImpl implements FuncionarioRepository {

    @Override
    public void salvar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (id_pessoa, id_cargo, login, senha, turno, telefone, email, rua, numero, cep) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, funcionario.getPessoa().getId());
            st.setInt(2, funcionario.getCargo().getId());
            st.setString(3, funcionario.getLogin());
            st.setString(4, funcionario.getSenha());
            st.setString(5, funcionario.getTurno());
            st.setString(6, funcionario.getTelefone());
            st.setString(7, funcionario.getEmail());
            st.setString(8, funcionario.getRua());
            st.setInt(9, funcionario.getNumero());
            st.setString(10, funcionario.getCep());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                funcionario.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar funcionário: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void atualizar(Funcionario funcionario) {
        String sql = "UPDATE funcionario SET id_cargo = ?, login = ?, senha = ?, turno = ?, "
                + "telefone = ?, email = ?, rua = ?, numero = ?, cep = ? WHERE id_funcionario = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, funcionario.getCargo().getId());
            st.setString(2, funcionario.getLogin());
            st.setString(3, funcionario.getSenha());
            st.setString(4, funcionario.getTurno());
            st.setString(5, funcionario.getTelefone());
            st.setString(6, funcionario.getEmail());
            st.setString(7, funcionario.getRua());
            st.setInt(8, funcionario.getNumero());
            st.setString(9, funcionario.getCep());
            st.setInt(10, funcionario.getId());
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar funcionário: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public Funcionario buscarPorId(int id) {
        String sql = "SELECT f.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "c.nome_cargo, c.descricao AS cargo_descricao "
                + "FROM funcionario f "
                + "JOIN pessoa p ON f.id_pessoa = p.id_pessoa "
                + "JOIN cargo c ON f.id_cargo = c.id_cargo "
                + "WHERE f.id_funcionario = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarFuncionario(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public Funcionario buscarPorCpfPessoa(String cpf) {
        String sql = "SELECT f.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "c.nome_cargo, c.descricao AS cargo_descricao "
                + "FROM funcionario f "
                + "JOIN pessoa p ON f.id_pessoa = p.id_pessoa "
                + "JOIN cargo c ON f.id_cargo = c.id_cargo "
                + "WHERE p.cpf = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, cpf);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarFuncionario(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário por CPF: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public Funcionario buscarPorLogin(String login) {
        String sql = "SELECT f.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "c.nome_cargo, c.descricao AS cargo_descricao "
                + "FROM funcionario f "
                + "JOIN pessoa p ON f.id_pessoa = p.id_pessoa "
                + "JOIN cargo c ON f.id_cargo = c.id_cargo "
                + "WHERE f.login = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarFuncionario(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionário por login: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public List<Funcionario> listarTodos() {
        String sql = "SELECT f.*, p.nome_completo, p.cpf, p.sexo, p.data_nascimento, p.data_cadastro, "
                + "c.nome_cargo, c.descricao AS cargo_descricao "
                + "FROM funcionario f "
                + "JOIN pessoa p ON f.id_pessoa = p.id_pessoa "
                + "JOIN cargo c ON f.id_cargo = c.id_cargo "
                + "ORDER BY p.nome_completo";
        List<Funcionario> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarFuncionario(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private Funcionario montarFuncionario(ResultSet rs) throws SQLException {
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

        Cargo c = new Cargo();
        c.setId(rs.getInt("id_cargo"));
        c.setNomeCargo(rs.getString("nome_cargo"));
        c.setDescricao(rs.getString("cargo_descricao"));

        Funcionario f = new Funcionario();
        f.setId(rs.getInt("id_funcionario"));
        f.setPessoa(p);
        f.setCargo(c);
        f.setLogin(rs.getString("login"));
        f.setSenha(rs.getString("senha"));
        f.setTurno(rs.getString("turno"));
        f.setTelefone(rs.getString("telefone"));
        f.setEmail(rs.getString("email"));
        f.setRua(rs.getString("rua"));
        f.setNumero(rs.getInt("numero"));
        f.setCep(rs.getString("cep"));
        return f;
    }
}
