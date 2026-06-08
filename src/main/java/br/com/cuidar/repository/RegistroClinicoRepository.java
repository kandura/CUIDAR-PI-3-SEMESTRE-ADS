package br.com.cuidar.repository;

import br.com.cuidar.model.RegistroClinico;
import br.com.cuidar.model.Residente;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos registros clínicos.
 */
public interface RegistroClinicoRepository {

    void salvar(RegistroClinico registro);

    void excluir(int id);

    RegistroClinico buscarPorId(int id);

    List<RegistroClinico> listarPorResidente(Residente residente);

    List<RegistroClinico> listarPorPeriodo(LocalDate inicio, LocalDate fim);
}
