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

    /**
     * Cria um novo prontuário vinculado ao residente.
     *
     * @param prontuario - prontuário a ser criado
     */
    public void criarProntuario(Prontuario prontuario) {
        prontuarioRepository.salvar(prontuario);
    }

    /**
     * Atualiza as informações do prontuário.
     *
     * @param prontuario - prontuário com os dados atualizados
     */
    public void atualizarProntuario(Prontuario prontuario) {
        prontuarioRepository.atualizar(prontuario);
    }

    /**
     * Busca o prontuário de um residente específico.
     *
     * @param residente - residente associado ao prontuário
     * @return - o prontuário do residente
     */
    public Prontuario buscarPorResidente(Residente residente) {
        return prontuarioRepository.buscarPorResidente(residente);
    }
}
