package br.com.cuidar.repository;

import br.com.cuidar.model.Quarto;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos quartos.
 */
public interface QuartoRepository {

    void salvar(Quarto quarto);

    void atualizar(Quarto quarto);

    Quarto buscarPorId(int id);

    List<Quarto> listarTodos();

    List<Quarto> listarPorStatus(String status);
}
