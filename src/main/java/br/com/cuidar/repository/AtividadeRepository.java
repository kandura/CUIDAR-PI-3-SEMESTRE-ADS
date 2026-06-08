package br.com.cuidar.repository;

import br.com.cuidar.model.Atividade;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados das atividades.
 */
public interface AtividadeRepository {

    void salvar(Atividade atividade);

    void atualizar(Atividade atividade);

    void excluir(int id);

    Atividade buscarPorId(int id);

    List<Atividade> listarTodos();

    List<Atividade> listarPorDiaSemana(String diaSemana);
}
