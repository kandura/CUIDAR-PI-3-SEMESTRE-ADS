package br.com.cuidar.controller;

import br.com.cuidar.model.Atividade;
import br.com.cuidar.service.AtividadeService;
import java.util.List;

/**
 * Controller responsável por receber as requisições da tela de Atividades
 * e delegar para a camada de serviço.
 */
public class AtividadeController {

    private final AtividadeService atividadeService;

    public AtividadeController(AtividadeService atividadeService) {
        this.atividadeService = atividadeService;
    }

    public void cadastrarAtividade(Atividade atividade) {
        atividadeService.cadastrarAtividade(atividade);
    }

    public void atualizarAtividade(Atividade atividade) {
        atividadeService.atualizarAtividade(atividade);
    }

    public void excluirAtividade(int id) {
        atividadeService.excluirAtividade(id);
    }

    public List<Atividade> listarTodos() {
        return atividadeService.listarTodos();
    }

    public List<Atividade> listarPorDia(String diaSemana) {
        return atividadeService.listarPorDia(diaSemana);
    }
}