package br.com.cuidar.controller;

import br.com.cuidar.model.Medicamento;
import br.com.cuidar.service.MedicamentoService;
import java.util.List;

/**
 * Controller responsável por receber as requisições da tela de Medicamentos
 * e delegar para a camada de serviço.
 */
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    public void cadastrarMedicamento(Medicamento medicamento) {
        medicamentoService.cadastrarMedicamento(medicamento);
    }

    public void atualizarMedicamento(Medicamento medicamento) {
        medicamentoService.atualizarMedicamento(medicamento);
    }

    public List<Medicamento> listarTodos() {
        return medicamentoService.listarTodos();
    }
}
