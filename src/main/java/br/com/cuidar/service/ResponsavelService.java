package br.com.cuidar.service;

import br.com.cuidar.model.Residente;
import br.com.cuidar.model.ResidenteResponsavel;
import br.com.cuidar.model.Responsavel;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.repository.ResponsavelRepository;
import br.com.cuidar.repository.ResidenteResponsavelRepository;
import br.com.cuidar.repository.PessoaRepository;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio dos responsáveis.
 */
public class ResponsavelService {

    private final ResponsavelRepository responsavelRepository;
    private final ResidenteResponsavelRepository residenteResponsavelRepository;
    private final PessoaRepository pessoaRepository;

    public ResponsavelService(ResponsavelRepository responsavelRepository,
                              ResidenteResponsavelRepository residenteResponsavelRepository,
                              PessoaRepository pessoaRepository) {
        this.responsavelRepository = responsavelRepository;
        this.residenteResponsavelRepository = residenteResponsavelRepository;
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Cadastra um novo responsável no sistema.
     * Primeiro salva a Pessoa, depois o Responsavel vinculado.
     *
     * @param responsavel - responsável a ser cadastrado
     */
    public void cadastrarResponsavel(Responsavel responsavel) {
        pessoaRepository.salvar(responsavel.getPessoa());
        responsavelRepository.salvar(responsavel);
    }

    /**
     * Vincula um responsável a um residente com o grau de parentesco.
     *
     * @param residenteResponsavel - vínculo a ser criado
     */
    public void vincularAoResidente(ResidenteResponsavel residenteResponsavel) {
        residenteResponsavelRepository.salvar(residenteResponsavel);
    }

    /**
     * Remove o vínculo entre responsável e residente.
     *
     * @param id - identificador do vínculo
     */
    public void desvincularDoResidente(int id) {
        residenteResponsavelRepository.excluir(id);
    }

    /**
     * Lista os responsáveis de um residente.
     *
     * @param residente - residente associado
     * @return - lista de vínculos residente-responsável
     */
    public List<ResidenteResponsavel> listarPorResidente(Residente residente) {
        return residenteResponsavelRepository.listarPorResidente(residente);
    }
}
