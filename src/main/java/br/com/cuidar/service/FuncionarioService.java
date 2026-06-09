package br.com.cuidar.service;

import br.com.cuidar.model.Funcionario;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.repository.FuncionarioRepository;
import br.com.cuidar.repository.PessoaRepository;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio dos funcionários.
 * Versão 3.1: persiste senha em texto puro (PBKDF2 entra na v3.6).
 */
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PessoaRepository pessoaRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository,
                              PessoaRepository pessoaRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public void cadastrarFuncionario(Funcionario funcionario) {
        Pessoa pessoa = funcionario.getPessoa();
        Pessoa existente = pessoaRepository.buscarPorCpf(pessoa.getCpf());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com o CPF: " + pessoa.getCpf());
        }
        if (funcionario.getSenha() == null || funcionario.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }
        pessoaRepository.salvar(pessoa);
        funcionarioRepository.salvar(funcionario);
    }

    public void editarFuncionario(Funcionario funcionario) {
        pessoaRepository.atualizar(funcionario.getPessoa());
        funcionarioRepository.atualizar(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.listarTodos();
    }
}
