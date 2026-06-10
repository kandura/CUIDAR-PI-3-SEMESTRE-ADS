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

    /**
     * Cadastra uma nova atividade na programação da ILPI.
     *
     * @param atividade - atividade a ser cadastrada
     */
    public void cadastrarAtividade(Atividade atividade) {
        atividadeRepository.salvar(atividade);
    }

    /**
     * Atualiza os dados de uma atividade existente.
     *
     * @param atividade - atividade com os dados atualizados
     */
    public void atualizarAtividade(Atividade atividade) {
        atividadeRepository.atualizar(atividade);
    }

    /**
     * Exclui uma atividade pelo ID.
     *
     * @param id - identificador da atividade
     */
    public void excluirAtividade(int id) {
        atividadeRepository.excluir(id);
    }

    /**
     * Lista todas as atividades cadastradas.
     *
     * @return lista de atividades
     */
    public List<Atividade> listarTodos() {
        return atividadeRepository.listarTodos();
    }

    /**
     * Lista as atividades de um dia da semana específico.
     *
     * @param diaSemana - dia da semana (ex: "Segunda", "Terça")
     * @return lista de atividades do dia
     */
    public List<Atividade> listarPorDia(String diaSemana) {
        return atividadeRepository.listarPorDiaSemana(diaSemana);
    }
}
