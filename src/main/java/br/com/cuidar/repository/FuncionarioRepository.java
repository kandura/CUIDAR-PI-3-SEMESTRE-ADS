package br.com.cuidar.repository;

import br.com.cuidar.model.Funcionario;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos funcionários.
 */
public interface FuncionarioRepository {

    void salvar(Funcionario funcionario);

    void atualizar(Funcionario funcionario);

    Funcionario buscarPorId(int id);

    Funcionario buscarPorCpfPessoa(String cpf);

    Funcionario buscarPorLogin(String login);

    List<Funcionario> listarTodos();
}
