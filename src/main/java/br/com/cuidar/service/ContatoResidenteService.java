package br.com.cuidar.service;

import br.com.cuidar.model.ContatoResidente;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.ContatoResidenteRepository;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio dos contatos de residentes.
 * Gerencia o cadastro, atualização e exclusão de contatos vinculados a residentes.
 */
public class ContatoResidenteService {

    private final ContatoResidenteRepository contatoResidenteRepository;

    public ContatoResidenteService(ContatoResidenteRepository contatoResidenteRepository) {
        this.contatoResidenteRepository = contatoResidenteRepository;
    }

    /**
     * Cadastra um novo contato vinculado a um residente.
     *
     * @param contato - contato a ser cadastrado
     */
    public void cadastrarContato(ContatoResidente contato) {
        contatoResidenteRepository.salvar(contato);
    }

    /**
     * Atualiza os dados de um contato existente.
     *
     * @param contato - contato com os dados atualizados
     */
    public void atualizarContato(ContatoResidente contato) {
        contatoResidenteRepository.atualizar(contato);
    }

    /**
     * Exclui um contato pelo ID.
     *
     * @param id - identificador do contato
     */
    public void excluirContato(int id) {
        contatoResidenteRepository.excluir(id);
    }

    /**
     * Lista todos os contatos de um residente.
     *
     * @param residente - residente associado aos contatos
     * @return - lista de contatos do residente
     */
    public List<ContatoResidente> listarPorResidente(Residente residente) {
        return contatoResidenteRepository.listarPorResidente(residente);
    }
}
