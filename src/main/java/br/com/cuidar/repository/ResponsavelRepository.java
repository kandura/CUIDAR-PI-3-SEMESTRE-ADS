package br.com.cuidar.repository;

import br.com.cuidar.model.Responsavel;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos responsáveis.
 */
public interface ResponsavelRepository {

    void salvar(Responsavel responsavel);

    void atualizar(Responsavel responsavel);

    Responsavel buscarPorId(int id);

    List<Responsavel> listarTodos();
}
