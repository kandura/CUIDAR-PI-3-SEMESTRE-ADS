package br.com.cuidar.repository;

import br.com.cuidar.model.Residente;
import br.com.cuidar.model.ResidenteResponsavel;
import br.com.cuidar.model.Responsavel;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados da associação residente-responsável.
 */
public interface ResidenteResponsavelRepository {

    void salvar(ResidenteResponsavel residenteResponsavel);

    void excluir(int id);

    List<ResidenteResponsavel> listarPorResidente(Residente residente);

    List<ResidenteResponsavel> listarPorResponsavel(Responsavel responsavel);
}
