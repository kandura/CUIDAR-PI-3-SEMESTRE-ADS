package br.com.cuidar.service;

import br.com.cuidar.model.Residente;
import br.com.cuidar.model.ResidenteResponsavel;
import br.com.cuidar.model.Responsavel;
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

    public void cadastrarResponsavel(Responsavel responsavel) {
        pessoaRepository.salvar(responsavel.getPessoa());
        responsavelRepository.salvar(responsavel);
    }

    public void vincularAoResidente(ResidenteResponsavel residenteResponsavel) {
        residenteResponsavelRepository.salvar(residenteResponsavel);
    }

    public void desvincularDoResidente(int id) {
        residenteResponsavelRepository.excluir(id);
    }

    public List<ResidenteResponsavel> listarPorResidente(Residente residente) {
        return residenteResponsavelRepository.listarPorResidente(residente);
    }
}
