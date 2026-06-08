package br.com.cuidar.repository;

import br.com.cuidar.model.Residente;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos residentes.
 */
public interface ResidenteRepository {

    void salvar(Residente residente);

    void atualizar(Residente residente);

    Residente buscarPorId(int id);

    Residente buscarPorCpfPessoa(String cpf);

    List<Residente> buscarPorNomePessoa(String nome);

    List<Residente> listarTodos();
}
