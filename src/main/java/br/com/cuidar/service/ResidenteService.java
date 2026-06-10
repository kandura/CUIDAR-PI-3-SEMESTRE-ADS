package br.com.cuidar.service;

import br.com.cuidar.model.Pessoa;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.PessoaRepository;
import br.com.cuidar.repository.ResidenteRepository;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio relacionadas aos residentes.
 */
public class ResidenteService {

    private final ResidenteRepository residenteRepository;
    private final PessoaRepository pessoaRepository;

    public ResidenteService(ResidenteRepository residenteRepository,
                            PessoaRepository pessoaRepository) {
        this.residenteRepository = residenteRepository;
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Cadastra um novo residente no sistema.
     * Primeiro salva a Pessoa, depois o Residente vinculado.
     * Valida unicidade de CPF (RN01).
     *
     * @param residente - residente a ser cadastrado
     */
    public void cadastrarResidente(Residente residente) {
        Pessoa pessoa = residente.getPessoa();
        Pessoa existente = pessoaRepository.buscarPorCpf(pessoa.getCpf());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com o CPF: " + pessoa.getCpf());
        }
        pessoaRepository.salvar(pessoa);
        residenteRepository.salvar(residente);
    }

    /**
     * Edita os dados de um residente existente.
     *
     * @param residente - residente com os dados atualizados
     */
    public void editarResidente(Residente residente) {
        pessoaRepository.atualizar(residente.getPessoa());
        residenteRepository.atualizar(residente);
    }

    /**
     * Busca um residente pelo CPF da pessoa vinculada.
     *
     * @param cpf - CPF da pessoa
     * @return - o residente encontrado ou null
     */
    public Residente buscarPorCpf(String cpf) {
        return residenteRepository.buscarPorCpfPessoa(cpf);
    }

    /**
     * Busca residentes pelo nome da pessoa vinculada.
     *
     * @param nome - nome ou parte do nome
     * @return - lista de residentes encontrados
     */
    public List<Residente> buscarPorNome(String nome) {
        return residenteRepository.buscarPorNomePessoa(nome);
    }

    /**
     * Lista todos os residentes cadastrados.
     *
     * @return - lista de todos os residentes
     */
    public List<Residente> listarTodos() {
        return residenteRepository.listarTodos();
    }
}
