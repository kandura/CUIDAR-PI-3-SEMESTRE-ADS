package br.com.cuidar.repository;

import br.com.cuidar.model.Prontuario;
import br.com.cuidar.model.Residente;

/**
 * Interface responsável pelo acesso a dados dos prontuários.
 */
public interface ProntuarioRepository {

    void salvar(Prontuario prontuario);

    void atualizar(Prontuario prontuario);

    Prontuario buscarPorId(int id);

    Prontuario buscarPorResidente(Residente residente);
}
