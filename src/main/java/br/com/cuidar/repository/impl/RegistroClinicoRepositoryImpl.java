package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.*;
import br.com.cuidar.repository.RegistroClinicoRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório de {@link RegistroClinico}.
 * Realiza operações de persistência na tabela {@code registro_clinico},
 * com JOINs em {@code residente}, {@code funcionario}, {@code medicamento} e {@code medico}.
 */
public class RegistroClinicoRepositoryImpl implements RegistroClinicoRepository {

    @Override
    public void salvar(RegistroClinico registro) {
        String sql = "INSERT INTO registro_clinico (id_residente, id_funcionario, id_medicamento, id_medico, "
                + "tipo_evento, intercorrencia, data_registro, dosagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, registro.getResidente().getId());
            st.setInt(2, registro.getFuncionario().getId());
            st.setInt(3, registro.getMedicamento().getId());
            if (registro.getMedico() != null) {
                st.setInt(4, registro.getMedico().getId());
            } else {
                st.setNull(4, Types.INTEGER);
            }
            st.setString(5, registro.getTipoEvento());
            st.setString(6, registro.getIntercorrencia());
            st.setDate(7, Date.valueOf(registro.getDataRegistro()));
            st.setString(8, registro.getDosagem());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                registro.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar registro clínico: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public RegistroClinico buscarPorId(int id) {
        String sql = montarSqlConsulta() + " WHERE rc.id_registro_clinico = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return montarRegistroClinico(rs);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar registro clínico: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return null;
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM registro_clinico WHERE id_registro_clinico = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir registro clínico: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public List<RegistroClinico> listarPorResidente(Residente residente) {
        String sql = montarSqlConsulta() + " WHERE rc.id_residente = ? ORDER BY rc.data_registro DESC";
        List<RegistroClinico> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, residente.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarRegistroClinico(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar registros por residente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    @Override
    public List<RegistroClinico> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = montarSqlConsulta() + " WHERE rc.data_registro BETWEEN ? AND ? ORDER BY rc.data_registro DESC";
        List<RegistroClinico> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setDate(1, Date.valueOf(inicio));
            st.setDate(2, Date.valueOf(fim));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                lista.add(montarRegistroClinico(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar registros por período: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    private String montarSqlConsulta() {
        return "SELECT rc.*, "
                + "pres.nome_completo AS res_nome, pres.cpf AS res_cpf, pres.sexo AS res_sexo, "
                + "pres.data_nascimento AS res_nasc, pres.data_cadastro AS res_cadastro, "
                + "res.status AS res_status, res.obs_geral AS res_obs, "
                + "q.id_quarto, q.numero AS quarto_numero, q.status AS quarto_status, "
                + "pfunc.nome_completo AS func_nome, pfunc.cpf AS func_cpf, "
                + "med.nome AS med_nome, med.fabricante, med.data_validade, med.quantidade, med.descricao AS med_descricao, "
                + "pmed.nome_completo AS medico_nome, m.crm, m.especialidade "
                + "FROM registro_clinico rc "
                + "JOIN residente res ON rc.id_residente = res.id_residente "
                + "JOIN pessoa pres ON res.id_pessoa = pres.id_pessoa "
                + "JOIN quarto q ON res.id_quarto = q.id_quarto "
                + "JOIN funcionario func ON rc.id_funcionario = func.id_funcionario "
                + "JOIN pessoa pfunc ON func.id_pessoa = pfunc.id_pessoa "
                + "JOIN medicamento med ON rc.id_medicamento = med.id_medicamento "
                + "LEFT JOIN medico m ON rc.id_medico = m.id_medico "
                + "LEFT JOIN pessoa pmed ON m.id_pessoa = pmed.id_pessoa";
    }

    private RegistroClinico montarRegistroClinico(ResultSet rs) throws SQLException {
        Pessoa pRes = new Pessoa();
        pRes.setNomeCompleto(rs.getString("res_nome"));
        pRes.setCpf(rs.getString("res_cpf"));
        pRes.setSexo(rs.getString("res_sexo"));
        pRes.setDataNascimento(rs.getDate("res_nasc").toLocalDate());

        Quarto q = new Quarto();
        q.setId(rs.getInt("id_quarto"));
        q.setNumero(rs.getInt("quarto_numero"));
        q.setStatus(rs.getString("quarto_status"));

        Residente residente = new Residente();
        residente.setId(rs.getInt("id_residente"));
        residente.setPessoa(pRes);
        residente.setQuarto(q);
        residente.setStatus(rs.getString("res_status"));
        residente.setObsGeral(rs.getString("res_obs"));

        Pessoa pFunc = new Pessoa();
        pFunc.setNomeCompleto(rs.getString("func_nome"));
        pFunc.setCpf(rs.getString("func_cpf"));

        Funcionario funcionario = new Funcionario();
        funcionario.setId(rs.getInt("id_funcionario"));
        funcionario.setPessoa(pFunc);

        Medicamento medicamento = new Medicamento();
        medicamento.setId(rs.getInt("id_medicamento"));
        medicamento.setNome(rs.getString("med_nome"));
        medicamento.setFabricante(rs.getString("fabricante"));
        medicamento.setDataValidade(rs.getDate("data_validade").toLocalDate());
        medicamento.setQuantidade(rs.getInt("quantidade"));
        medicamento.setDescricao(rs.getString("med_descricao"));

        Medico medico = null;
        int idMedico = rs.getInt("id_medico");
        if (!rs.wasNull()) {
            Pessoa pMed = new Pessoa();
            pMed.setNomeCompleto(rs.getString("medico_nome"));

            medico = new Medico();
            medico.setId(idMedico);
            medico.setPessoa(pMed);
            medico.setCrm(rs.getString("crm"));
            medico.setEspecialidade(rs.getString("especialidade"));
        }

        RegistroClinico rc = new RegistroClinico();
        rc.setId(rs.getInt("id_registro_clinico"));
        rc.setResidente(residente);
        rc.setFuncionario(funcionario);
        rc.setMedicamento(medicamento);
        rc.setMedico(medico);
        rc.setTipoEvento(rs.getString("tipo_evento"));
        rc.setIntercorrencia(rs.getString("intercorrencia"));
        rc.setDataRegistro(rs.getDate("data_registro").toLocalDate());
        rc.setDosagem(rs.getString("dosagem"));
        return rc;
    }
}
