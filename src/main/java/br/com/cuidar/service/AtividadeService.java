package br.com.cuidar.service;

import br.com.cuidar.model.Atividade;
import br.com.cuidar.repository.AtividadeRepository;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio das atividades.
 */
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;

    public AtividadeService(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    public void cadastrarAtividade(Atividade atividade) {
        atividadeRepository.salvar(atividade);
    }

    public void atualizarAtividade(Atividade atividade) {
        atividadeRepository.atualizar(atividade);
    }

    public void excluirAtividade(int id) {
        atividadeRepository.excluir(id);
    }

    public List<Atividade> listarTodos() {
        return atividadeRepository.listarTodos();
    }

    public List<Atividade> listarPorDia(String diaSemana) {
        return atividadeRepository.listarPorDiaSemana(diaSemana);
    }
}
