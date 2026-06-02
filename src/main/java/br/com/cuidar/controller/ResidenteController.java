package br.com.cuidar.controller;

import br.com.cuidar.model.ContatoResidente;
import br.com.cuidar.model.Residente;
import br.com.cuidar.service.ContatoResidenteService;
import br.com.cuidar.service.ResidenteService;
import java.util.List;

/**
 * Controller responsável por receber as requisições da tela de Residentes
 * e delegar para a camada de serviço.
 */
public class ResidenteController {

    private final ResidenteService residenteService;
    private final ContatoResidenteService contatoResidenteService;

    public ResidenteController(ResidenteService residenteService,
                               ContatoResidenteService contatoResidenteService) {
        this.residenteService = residenteService;
        this.contatoResidenteService = contatoResidenteService;
    }

    /**
     * Cadastra um novo residente.
     *
     * @param residente - residente a ser cadastrado
     */
    public void cadastrarResidente(Residente residente) {
        residenteService.cadastrarResidente(residente);
    }

    /**
     * Edita os dados de um residente.
     *
     * @param residente - residente com os dados atualizados
     */
    public void editarResidente(Residente residente) {
        residenteService.editarResidente(residente);
    }

    /**
     * Inativa um residente (soft delete).
     *
     * @param residente - residente a ser inativado
     */
    public void inativarResidente(Residente residente) {
        residenteService.inativarResidente(residente);
    }

    /**
     * Busca um residente pelo CPF.
     *
     * @param cpf - CPF do residente
     * @return - o residente encontrado
     */
    public Residente buscarPorCpf(String cpf) {
        return residenteService.buscarPorCpf(cpf);
    }

    /**
     * Busca residentes pelo nome.
     *
     * @param nome - nome ou parte do nome
     * @return - lista de residentes encontrados
     */
    public List<Residente> buscarPorNome(String nome) {
        return residenteService.buscarPorNome(nome);
    }

    /**
     * Lista todos os residentes.
     *
     * @return - lista de residentes
     */
    public List<Residente> listarTodos() {
        return residenteService.listarTodos();
    }

    /**
     * Lista apenas os residentes ativos.
     *
     * @return - lista de residentes ativos
     */
    public List<Residente> listarAtivos() {
        return residenteService.listarAtivos();
    }

    /**
     * Cadastra um contato vinculado a um residente.
     *
     * @param contato - contato a ser cadastrado
     */
    public void cadastrarContato(ContatoResidente contato) {
        contatoResidenteService.cadastrarContato(contato);
    }

    /**
     * Atualiza os dados de um contato.
     *
     * @param contato - contato com os dados atualizados
     */
    public void atualizarContato(ContatoResidente contato) {
        contatoResidenteService.atualizarContato(contato);
    }

    /**
     * Exclui um contato pelo ID.
     *
     * @param id - identificador do contato
     */
    public void excluirContato(int id) {
        contatoResidenteService.excluirContato(id);
    }

    /**
     * Lista os contatos de um residente.
     *
     * @param residente - residente associado aos contatos
     * @return - lista de contatos
     */
    public List<ContatoResidente> listarContatos(Residente residente) {
        return contatoResidenteService.listarPorResidente(residente);
    }
}
