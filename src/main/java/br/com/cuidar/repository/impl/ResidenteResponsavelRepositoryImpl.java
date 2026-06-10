package br.com.cuidar.repository.impl;

import br.com.cuidar.config.ConnectionFactory;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.model.Quarto;
import br.com.cuidar.model.Residente;
import br.com.cuidar.model.ResidenteResponsavel;
import br.com.cuidar.model.Responsavel;
import br.com.cuidar.repository.ResidenteResponsavelRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação JDBC do repositório da associação {@link ResidenteResponsavel}.
 * Realiza operações na tabela {@code residente_responsavel}.
 */
public class ResidenteResponsavelRepositoryImpl implements ResidenteResponsavelRepository {

    @Override
    public void salvar(ResidenteResponsavel rr) {
        String sql = "INSERT INTO residente_responsavel (id_residente, id_responsavel, parentesco) VALUES (?, ?, ?)";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, rr.getResidente().getId());
            st.setInt(2, rr.getResponsavel().getId());
            st.setString(3, rr.getParentesco());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                rr.setId(rs.getInt(1));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar vínculo residente-responsável: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM residente_responsavel WHERE id_residente_responsavel = ?";
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir vínculo residente-responsável: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
    }

    @Override
    public List<ResidenteResponsavel> listarPorResidente(Residente residente) {
        String sql = "SELECT rr.*, "
                + "pr.nome_completo AS resp_nome, pr.cpf AS resp_cpf, pr.sexo AS resp_sexo, "
                + "pr.data_nascimento AS resp_nasc, pr.data_cadastro AS resp_cadastro, "
                + "resp.telefone AS resp_telefone, resp.email AS resp_email, "
                + "resp.rua AS resp_rua, resp.numero AS resp_numero, resp.bairro AS resp_bairro, "
                + "resp.cidade AS resp_cidade, resp.estado AS resp_estado, resp.cep AS resp_cep "
                + "FROM residente_responsavel rr "
                + "JOIN responsavel resp ON rr.id_responsavel = resp.id_responsavel "
                + "JOIN pessoa pr ON resp.id_pessoa = pr.id_pessoa "
                + "WHERE rr.id_residente = ?";
        List<ResidenteResponsavel> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, residente.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Pessoa pResp = new Pessoa();
                pResp.setId(rs.getInt("id_pessoa"));
                pResp.setNomeCompleto(rs.getString("resp_nome"));
                pResp.setCpf(rs.getString("resp_cpf"));
                pResp.setSexo(rs.getString("resp_sexo"));
                pResp.setDataNascimento(rs.getDate("resp_nasc").toLocalDate());
                Timestamp ts = rs.getTimestamp("resp_cadastro");
                if (ts != null) {
                    pResp.setDataCadastro(ts.toLocalDateTime());
                }

                Responsavel resp = new Responsavel();
                resp.setId(rs.getInt("id_responsavel"));
                resp.setPessoa(pResp);
                resp.setTelefone(rs.getString("resp_telefone"));
                resp.setEmail(rs.getString("resp_email"));
                resp.setRua(rs.getString("resp_rua"));
                resp.setNumero(rs.getInt("resp_numero"));
                resp.setBairro(rs.getString("resp_bairro"));
                resp.setCidade(rs.getString("resp_cidade"));
                resp.setEstado(rs.getString("resp_estado"));
                resp.setCep(rs.getString("resp_cep"));

                ResidenteResponsavel rr2 = new ResidenteResponsavel();
                rr2.setId(rs.getInt("id_residente_responsavel"));
                rr2.setResidente(residente);
                rr2.setResponsavel(resp);
                rr2.setParentesco(rs.getString("parentesco"));
                lista.add(rr2);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar responsáveis do residente: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }

    @Override
    public List<ResidenteResponsavel> listarPorResponsavel(Responsavel responsavel) {
        String sql = "SELECT rr.*, "
                + "pres.nome_completo AS res_nome, pres.cpf AS res_cpf, pres.sexo AS res_sexo, "
                + "pres.data_nascimento AS res_nasc, pres.data_cadastro AS res_cadastro, "
                + "r.status AS res_status, r.obs_geral AS res_obs, "
                + "q.id_quarto, q.numero AS quarto_numero, q.status AS quarto_status "
                + "FROM residente_responsavel rr "
                + "JOIN residente r ON rr.id_residente = r.id_residente "
                + "JOIN pessoa pres ON r.id_pessoa = pres.id_pessoa "
                + "JOIN quarto q ON r.id_quarto = q.id_quarto "
                + "WHERE rr.id_responsavel = ?";
        List<ResidenteResponsavel> lista = new ArrayList<>();
        Connection con = null;
        try {
            con = ConnectionFactory.getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, responsavel.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Pessoa pRes = new Pessoa();
                pRes.setId(rs.getInt("id_pessoa"));
                pRes.setNomeCompleto(rs.getString("res_nome"));
                pRes.setCpf(rs.getString("res_cpf"));
                pRes.setSexo(rs.getString("res_sexo"));
                pRes.setDataNascimento(rs.getDate("res_nasc").toLocalDate());
                Timestamp ts = rs.getTimestamp("res_cadastro");
                if (ts != null) {
                    pRes.setDataCadastro(ts.toLocalDateTime());
                }

                Quarto q = new Quarto();
                q.setId(rs.getInt("id_quarto"));
                q.setNumero(rs.getInt("quarto_numero"));
                q.setStatus(rs.getString("quarto_status"));

                Residente res = new Residente();
                res.setId(rs.getInt("id_residente"));
                res.setPessoa(pRes);
                res.setQuarto(q);
                res.setStatus(rs.getString("res_status"));
                res.setObsGeral(rs.getString("res_obs"));

                ResidenteResponsavel rr2 = new ResidenteResponsavel();
                rr2.setId(rs.getInt("id_residente_responsavel"));
                rr2.setResidente(res);
                rr2.setResponsavel(responsavel);
                rr2.setParentesco(rs.getString("parentesco"));
                lista.add(rr2);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar residentes do responsável: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(con);
        }
        return lista;
    }
}
