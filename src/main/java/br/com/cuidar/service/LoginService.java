package br.com.cuidar.service;

import br.com.cuidar.model.Funcionario;
import br.com.cuidar.repository.FuncionarioRepository;

/**
 * Camada de serviço responsável pela autenticação de funcionários.
 * Versão 3.1: comparação direta de senha em texto puro
 * (o hash PBKDF2 será introduzido na v3.6 junto do {@code PasswordUtil}).
 */
public class LoginService {

    private final FuncionarioRepository funcionarioRepository;

    public LoginService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    /**
     * Autentica um funcionário pelo login e senha.
     *
     * @param login - login do funcionário
     * @param senha - senha do funcionário
     * @return o funcionário autenticado ou null se credenciais inválidas
     */
    public Funcionario autenticar(String login, String senha) {
        Funcionario funcionario = funcionarioRepository.buscarPorLogin(login);
        if (funcionario == null) return null;
        String stored = funcionario.getSenha();
        if (stored != null && stored.equals(senha)) {
            return funcionario;
        }
        return null;
    }
}
