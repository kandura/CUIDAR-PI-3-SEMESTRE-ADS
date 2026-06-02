package br.com.cuidar.service;

import br.com.cuidar.model.Prontuario;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.ResidenteRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio relacionadas aos residentes.
 * Orquestra as operações entre o controller e o repository.
 */
public class ResidenteService {

    private final ResidenteRepository residenteRepository;
    private final ProntuarioService prontuarioService;

    public ResidenteService(ResidenteRepository residenteRepository,
                            ProntuarioService prontuarioService) {
        this.residenteRepository = residenteRepository;
        this.prontuarioService = prontuarioService;
    }

    /**
     * Cadastra um novo residente no sistema.
     * Valida se já existe um residente com o mesmo CPF antes de salvar (RN01).
     *
     * @param residente - residente a ser cadastrado
     * @throws IllegalArgumentException se já existir um residente com o mesmo CPF
     */
    public void cadastrarResidente(Residente residente) {
        Residente existente = residenteRepository.buscarPorCpf(residente.getCpf());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe um residente cadastrado com o CPF: " + residente.getCpf());
        }
        residenteRepository.salvar(residente);

        // Cria o prontuário automaticamente na admissão (RN04)
        Prontuario prontuario = new Prontuario();
        prontuario.setResidente(residente);
        prontuario.setDataCriacao(LocalDate.now());
        prontuarioService.criarProntuario(prontuario);
    }

    /**
     * Edita os dados de um residente existente.
     *
     * @param residente - residente com os dados atualizados
     */
    public void editarResidente(Residente residente) {
        residenteRepository.atualizar(residente);
    }

    /**
     * Inativa um residente em vez de excluí-lo (RN02 - Soft Delete).
     *
     * @param residente - residente a ser inativado
     */
    public void inativarResidente(Residente residente) {
        residente.inativarPessoa();
        residenteRepository.atualizar(residente);
    }

    /**
     * Busca um residente pelo CPF.
     *
     * @param cpf - CPF do residente
     * @return - o residente encontrado ou null
     */
    public Residente buscarPorCpf(String cpf) {
        return residenteRepository.buscarPorCpf(cpf);
    }

    /**
     * Busca residentes pelo nome.
     *
     * @param nome - nome ou parte do nome do residente
     * @return - lista de residentes encontrados
     */
    public List<Residente> buscarPorNome(String nome) {
        return residenteRepository.buscarPorNome(nome);
    }

    /**
     * Lista todos os residentes cadastrados.
     *
     * @return - lista de todos os residentes
     */
    public List<Residente> listarTodos() {
        return residenteRepository.listarTodos();
    }

    /**
     * Lista apenas os residentes ativos.
     *
     * @return - lista de residentes ativos
     */
    public List<Residente> listarAtivos() {
        return residenteRepository.listarAtivos();
    }

    /**
     * Consulta o tempo de permanência do residente na instituição.
     *
     * @param residente - residente a ser consultado
     * @return - tempo em anos na instituição
     */
    public int consultarTempoResidente(Residente residente) {
        return residente.consultaTempoResidente(residente.getDataEntradaInstituicao());
    }
}
