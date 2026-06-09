package br.com.cuidar.service;

import br.com.cuidar.model.Prontuario;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.ProntuarioRepository;

/**
 * Camada de serviço responsável pelas regras de negócio relacionadas aos prontuários.
 */
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository) {
        this.prontuarioRepository = prontuarioRepository;
    }

    public void criarProntuario(Prontuario prontuario) {
        prontuarioRepository.salvar(prontuario);
    }

    public void atualizarProntuario(Prontuario prontuario) {
        prontuarioRepository.atualizar(prontuario);
    }

    public Prontuario buscarPorResidente(Residente residente) {
        return prontuarioRepository.buscarPorResidente(residente);
    }
}
