package br.com.cuidar.repository;

import br.com.cuidar.model.Medicamento;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos medicamentos.
 */
public interface MedicamentoRepository {

    void salvar(Medicamento medicamento);

    void atualizar(Medicamento medicamento);

    Medicamento buscarPorId(int id);

    List<Medicamento> listarTodos();
}
