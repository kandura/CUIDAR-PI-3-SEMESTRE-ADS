package br.com.cuidar.repository;

import br.com.cuidar.model.Prontuario;
import br.com.cuidar.model.Residente;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos prontuários.
 * Define as operações de persistência para a entidade Prontuario.
 */
public interface ProntuarioRepository {

    /**
     * Salva um novo prontuário no banco de dados.
     *
     * @param prontuario - prontuário a ser salvo
     */
    void salvar(Prontuario prontuario);

    /**
     * Atualiza os dados de um prontuário existente.
     *
     * @param prontuario - prontuário com os dados atualizados
     */
    void atualizar(Prontuario prontuario);

    /**
     * Busca o prontuário de um residente específico.
     *
     * @param residente - residente associado ao prontuário
     * @return - o prontuário encontrado ou null se não existir
     */
    Prontuario buscarPorResidente(Residente residente);

    /**
     * Busca um prontuário pelo ID.
     *
     * @param id - identificador do prontuário
     * @return - o prontuário encontrado ou null se não existir
     */
    Prontuario buscarPorId(int id);

    /**
     * Lista todos os prontuários cadastrados.
     *
     * @return - lista com todos os prontuários
     */
    List<Prontuario> listarTodos();
}
