package br.com.cuidar.service;

import br.com.cuidar.model.Prontuario;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.ProntuarioRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio relacionadas aos prontuários.
 * Orquestra as operações entre o controller e o repository.
 */
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository) {
        this.prontuarioRepository = prontuarioRepository;
    }

    /**
     * Cria um novo prontuário vinculado ao residente (RN04 - criado na admissão).
     *
     * @param prontuario - prontuário a ser criado
     */
    public void criarProntuario(Prontuario prontuario) {
        prontuarioRepository.salvar(prontuario);
    }

    /**
     * Atualiza as informações fixas do prontuário (doenças, alergias, condições médicas).
     *
     * @param prontuario - prontuário com os dados atualizados
     */
    public void atualizarInformacoesFixas(Prontuario prontuario) {
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

    /**
     * Lista todos os prontuários cadastrados.
     *
     * @return - lista de prontuários
     */
    public List<Prontuario> listarTodos() {
        return prontuarioRepository.listarTodos();
    }
}
