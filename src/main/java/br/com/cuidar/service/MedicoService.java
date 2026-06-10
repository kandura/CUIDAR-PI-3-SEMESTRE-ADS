package br.com.cuidar.service;

import br.com.cuidar.model.Medico;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.repository.MedicoRepository;
import br.com.cuidar.repository.PessoaRepository;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio dos médicos.
 */
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final PessoaRepository pessoaRepository;

    public MedicoService(MedicoRepository medicoRepository, PessoaRepository pessoaRepository) {
        this.medicoRepository = medicoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Cadastra um novo médico no sistema.
     * Primeiro salva a Pessoa, depois o Medico vinculado.
     *
     * @param medico - médico a ser cadastrado
     */
    public void cadastrarMedico(Medico medico) {
        Pessoa pessoa = medico.getPessoa();
        Pessoa existente = pessoaRepository.buscarPorCpf(pessoa.getCpf());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com o CPF: " + pessoa.getCpf());
        }
        pessoaRepository.salvar(pessoa);
        medicoRepository.salvar(medico);
    }

    /**
     * Edita os dados de um médico existente.
     *
     * @param medico - médico com os dados atualizados
     */
    public void editarMedico(Medico medico) {
        pessoaRepository.atualizar(medico.getPessoa());
        medicoRepository.atualizar(medico);
    }

    /**
     * Busca um médico pelo CRM.
     *
     * @param crm - CRM do médico
     * @return o médico encontrado ou null
     */
    public Medico buscarPorCrm(String crm) {
        return medicoRepository.buscarPorCrm(crm);
    }

    /**
     * Lista todos os médicos cadastrados.
     *
     * @return lista de médicos
     */
    public List<Medico> listarTodos() {
        return medicoRepository.listarTodos();
    }
}
