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

    public void adicionarRegistro(RegistroClinico registro) {
        registroClinicoRepository.salvar(registro);
    }

    public void excluirRegistro(int id) {
        registroClinicoRepository.excluir(id);
    }

    public List<RegistroClinico> listarPorResidente(Residente residente) {
        return registroClinicoRepository.listarPorResidente(residente);
    }

    public List<RegistroClinico> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return registroClinicoRepository.listarPorPeriodo(inicio, fim);
    }
}
