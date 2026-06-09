package br.com.cuidar.controller;

import br.com.cuidar.model.Medico;
import br.com.cuidar.service.MedicoService;
import java.util.List;

/**
 * Controller responsável por receber as requisições da tela de Médicos
 * e delegar para a camada de serviço.
 */
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    public void cadastrarMedico(Medico medico) {
        medicoService.cadastrarMedico(medico);
    }

    public void editarMedico(Medico medico) {
        medicoService.editarMedico(medico);
    }

    public Medico buscarPorCrm(String crm) {
        return medicoService.buscarPorCrm(crm);
    }

    public List<Medico> listarTodos() {
        return medicoService.listarTodos();
    }
}
