package br.com.cuidar.controller;

import br.com.cuidar.model.Prontuario;
import br.com.cuidar.model.Residente;
import br.com.cuidar.service.ProntuarioService;

/**
 * Controller responsável por receber as requisições da tela de Prontuário
 * e delegar para a camada de serviço.
 */
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    public void criarProntuario(Prontuario prontuario) {
        prontuarioService.criarProntuario(prontuario);
    }

    public void atualizarProntuario(Prontuario prontuario) {
        prontuarioService.atualizarProntuario(prontuario);
    }

    public Prontuario buscarPorResidente(Residente residente) {
        return prontuarioService.buscarPorResidente(residente);
    }
}
