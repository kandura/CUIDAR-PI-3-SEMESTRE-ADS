package br.com.cuidar.controller;

import br.com.cuidar.model.RegistroClinico;
import br.com.cuidar.model.Residente;
import br.com.cuidar.service.RegistroClinicoService;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsável por receber as requisições da tela de Registros Clínicos
 * e delegar para a camada de serviço.
 */
public class RegistroClinicoController {

    private final RegistroClinicoService registroClinicoService;

    public RegistroClinicoController(RegistroClinicoService registroClinicoService) {
        this.registroClinicoService = registroClinicoService;
    }

    public void adicionarRegistro(RegistroClinico registro) {
        registroClinicoService.adicionarRegistro(registro);
    }

    public void excluirRegistro(int id) {
        registroClinicoService.excluirRegistro(id);
    }

    public List<RegistroClinico> listarPorResidente(Residente residente) {
        return registroClinicoService.listarPorResidente(residente);
    }

    public List<RegistroClinico> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return registroClinicoService.listarPorPeriodo(inicio, fim);
    }
}
