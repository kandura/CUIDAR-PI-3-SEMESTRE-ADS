package br.com.cuidar.repository;

import br.com.cuidar.model.Pessoa;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados de pessoas.
 */
public interface PessoaRepository {

    void salvar(Pessoa pessoa);

    void atualizar(Pessoa pessoa);

    Pessoa buscarPorId(int id);

    Pessoa buscarPorCpf(String cpf);

    List<Pessoa> listarTodos();
}
