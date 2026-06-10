package br.com.cuidar.service;

import br.com.cuidar.model.Funcionario;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.repository.FuncionarioRepository;
import br.com.cuidar.repository.PessoaRepository;
import br.com.cuidar.util.PasswordUtil;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio dos funcionários.
 */
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PessoaRepository pessoaRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository,
                              PessoaRepository pessoaRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Cadastra um novo funcionário no sistema.
     * Primeiro salva a Pessoa, depois o Funcionário vinculado.
     *
     * @param funcionario - funcionário a ser cadastrado
     */
    public void cadastrarFuncionario(Funcionario funcionario) {
        Pessoa pessoa = funcionario.getPessoa();
        Pessoa existente = pessoaRepository.buscarPorCpf(pessoa.getCpf());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com o CPF: " + pessoa.getCpf());
        }
        if (funcionario.getSenha() == null || funcionario.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }
        if (!PasswordUtil.isHashed(funcionario.getSenha())) {
            funcionario.setSenha(PasswordUtil.hash(funcionario.getSenha()));
        }
        pessoaRepository.salvar(pessoa);
        funcionarioRepository.salvar(funcionario);
    }

    /**
     * Edita os dados de um funcionário existente.
     *
     * @param funcionario - funcionário com os dados atualizados
     */
    public void editarFuncionario(Funcionario funcionario) {
        if (funcionario.getSenha() != null && !funcionario.getSenha().isBlank()
                && !PasswordUtil.isHashed(funcionario.getSenha())) {
            funcionario.setSenha(PasswordUtil.hash(funcionario.getSenha()));
        }
        pessoaRepository.atualizar(funcionario.getPessoa());
        funcionarioRepository.atualizar(funcionario);
    }

    /**
     * Lista todos os funcionários cadastrados.
     *
     * @return - lista de funcionários
     */
    public List<Funcionario> listarTodos() {
        return funcionarioRepository.listarTodos();
    }
}