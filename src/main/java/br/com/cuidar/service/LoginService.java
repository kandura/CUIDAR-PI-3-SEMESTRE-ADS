package br.com.cuidar.service;

import br.com.cuidar.model.Funcionario;
import br.com.cuidar.repository.FuncionarioRepository;
import br.com.cuidar.util.PasswordUtil;

/**
 * Camada de serviço responsável pela autenticação de funcionários.
 * Suporta migração transparente de senhas legadas em texto puro para hash PBKDF2.
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

        if (PasswordUtil.isHashed(stored)) {
            return PasswordUtil.verify(senha, stored) ? funcionario : null;
        }
        // Senha legada em texto puro: valida, re-hasheia e persiste.
        if (stored != null && stored.equals(senha)) {
            funcionario.setSenha(PasswordUtil.hash(senha));
            funcionarioRepository.atualizar(funcionario);
            return funcionario;
        }
        return null;
    }
}
