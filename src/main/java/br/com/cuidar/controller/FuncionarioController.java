package br.com.cuidar.controller;

import br.com.cuidar.model.Funcionario;
import br.com.cuidar.service.FuncionarioService;
import java.util.List;

/**
 * Controller responsável por receber as requisições da tela de Funcionários
 * e delegar para a camada de serviço.
 */
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    public void cadastrarFuncionario(Funcionario funcionario) {
        funcionarioService.cadastrarFuncionario(funcionario);
    }

    public void editarFuncionario(Funcionario funcionario) {
        funcionarioService.editarFuncionario(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioService.listarTodos();
    }
}
