package br.com.cuidar.controller;

import br.com.cuidar.model.Residente;
import br.com.cuidar.model.ResidenteResponsavel;
import br.com.cuidar.service.ResidenteService;
import br.com.cuidar.service.ResponsavelService;
import java.util.List;

/**
 * Controller responsável por receber as requisições da tela de Residentes
 * e delegar para a camada de serviço.
 */
public class ResidenteController {

    private final ResidenteService residenteService;
    private final ResponsavelService responsavelService;

    public ResidenteController(ResidenteService residenteService,
                               ResponsavelService responsavelService) {
        this.residenteService = residenteService;
        this.responsavelService = responsavelService;
    }

    public void cadastrarResidente(Residente residente) {
        residenteService.cadastrarResidente(residente);
    }

    public void editarResidente(Residente residente) {
        residenteService.editarResidente(residente);
    }

    public Residente buscarPorCpf(String cpf) {
        return residenteService.buscarPorCpf(cpf);
    }

    public List<Residente> buscarPorNome(String nome) {
        return residenteService.buscarPorNome(nome);
    }

    public List<Residente> listarTodos() {
        return residenteService.listarTodos();
    }

    public void vincularResponsavel(ResidenteResponsavel vinculo) {
        responsavelService.vincularAoResidente(vinculo);
    }

    public List<ResidenteResponsavel> listarResponsaveis(Residente residente) {
        return responsavelService.listarPorResidente(residente);
    }
}
