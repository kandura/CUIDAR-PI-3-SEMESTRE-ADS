package br.com.cuidar.repository;

import br.com.cuidar.model.Medico;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos médicos.
 */
public interface MedicoRepository {

    void salvar(Medico medico);

    void atualizar(Medico medico);

    Medico buscarPorId(int id);

    Medico buscarPorCrm(String crm);

    List<Medico> listarTodos();
}
