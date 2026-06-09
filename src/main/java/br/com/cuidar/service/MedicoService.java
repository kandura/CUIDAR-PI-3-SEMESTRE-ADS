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

    public void cadastrarMedico(Medico medico) {
        Pessoa pessoa = medico.getPessoa();
        Pessoa existente = pessoaRepository.buscarPorCpf(pessoa.getCpf());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com o CPF: " + pessoa.getCpf());
        }
        pessoaRepository.salvar(pessoa);
        medicoRepository.salvar(medico);
    }

    public void editarMedico(Medico medico) {
        pessoaRepository.atualizar(medico.getPessoa());
        medicoRepository.atualizar(medico);
    }

    public Medico buscarPorCrm(String crm) {
        return medicoRepository.buscarPorCrm(crm);
    }

    public List<Medico> listarTodos() {
        return medicoRepository.listarTodos();
    }
}
