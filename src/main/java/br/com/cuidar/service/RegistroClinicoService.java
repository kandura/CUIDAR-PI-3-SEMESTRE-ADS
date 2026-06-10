package br.com.cuidar.service;

import br.com.cuidar.model.RegistroClinico;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.RegistroClinicoRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio dos registros clínicos.
 */
public class RegistroClinicoService {

    private final RegistroClinicoRepository registroClinicoRepository;

    public RegistroClinicoService(RegistroClinicoRepository registroClinicoRepository) {
        this.registroClinicoRepository = registroClinicoRepository;
    }

    /**
     * Adiciona um novo registro clínico ao histórico do residente.
     *
     * @param registro - registro clínico a ser adicionado
     */
    public void adicionarRegistro(RegistroClinico registro) {
        registroClinicoRepository.salvar(registro);
    }

    /**
     * Exclui um registro clínico pelo ID.
     *
     * @param id - ID do registro a ser excluído
     */
    public void excluirRegistro(int id) {
        registroClinicoRepository.excluir(id);
    }

    /**
     * Lista o histórico completo de registros clínicos de um residente.
     *
     * @param residente - residente associado aos registros
     * @return - lista de registros clínicos do residente
     */
    public List<RegistroClinico> listarPorResidente(Residente residente) {
        return registroClinicoRepository.listarPorResidente(residente);
    }

    /**
     * Lista os registros clínicos filtrados por um período de datas.
     *
     * @param inicio - data de início do período
     * @param fim    - data de fim do período
     * @return - lista de registros no período informado
     */
    public List<RegistroClinico> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return registroClinicoRepository.listarPorPeriodo(inicio, fim);
    }
}
