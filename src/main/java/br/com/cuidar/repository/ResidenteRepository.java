package br.com.cuidar.repository;

import br.com.cuidar.model.Residente;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos residentes.
 * Define as operações de persistência (CRUD) para a entidade Residente.
 */
public interface ResidenteRepository {

    /**
     * Salva um novo residente no banco de dados.
     *
     * @param residente - residente a ser salvo
     */
    void salvar(Residente residente);

    /**
     * Atualiza os dados de um residente existente.
     *
     * @param residente - residente com os dados atualizados
     */
    void atualizar(Residente residente);

    /**
     * Busca um residente pelo CPF.
     *
     * @param cpf - CPF do residente
     * @return - o residente encontrado ou null se não existir
     */
    Residente buscarPorCpf(String cpf);

    /**
     * Busca residentes pelo nome (busca parcial).
     *
     * @param nome - nome ou parte do nome do residente
     * @return - lista de residentes encontrados
     */
    List<Residente> buscarPorNome(String nome);

    /**
     * Lista todos os residentes cadastrados.
     *
     * @return - lista com todos os residentes
     */
    List<Residente> listarTodos();

    /**
     * Lista apenas os residentes ativos.
     *
     * @return - lista de residentes com status ativo
     */
    List<Residente> listarAtivos();
}
