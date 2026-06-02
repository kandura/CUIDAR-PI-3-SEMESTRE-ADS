package br.com.cuidar.repository;

import br.com.cuidar.model.ContatoResidente;
import br.com.cuidar.model.Residente;
import java.util.List;

/**
 * Interface responsável pelo acesso a dados dos contatos de residentes.
 * Define as operações de persistência para a entidade ContatoResidente.
 */
public interface ContatoResidenteRepository {

    /**
     * Salva um novo contato no banco de dados.
     *
     * @param contato - contato a ser salvo
     */
    void salvar(ContatoResidente contato);

    /**
     * Atualiza os dados de um contato existente.
     *
     * @param contato - contato com os dados atualizados
     */
    void atualizar(ContatoResidente contato);

    /**
     * Exclui um contato pelo ID.
     *
     * @param id - identificador do contato a ser excluído
     */
    void excluir(int id);

    /**
     * Lista todos os contatos de um residente.
     *
     * @param residente - residente associado aos contatos
     * @return - lista de contatos do residente
     */
    List<ContatoResidente> listarPorResidente(Residente residente);

    /**
     * Busca um contato pelo ID.
     *
     * @param id - identificador do contato
     * @return - o contato encontrado ou null se não existir
     */
    ContatoResidente buscarPorId(int id);
}
