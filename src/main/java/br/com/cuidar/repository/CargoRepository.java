package br.com.cuidar.repository;

import br.com.cuidar.model.Cargo;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos cargos.
 */
public interface CargoRepository {

    void salvar(Cargo cargo);

    void atualizar(Cargo cargo);

    Cargo buscarPorId(int id);

    List<Cargo> listarTodos();
}
