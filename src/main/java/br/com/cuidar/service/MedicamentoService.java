package br.com.cuidar.service;

import br.com.cuidar.model.Medicamento;
import br.com.cuidar.repository.MedicamentoRepository;
import java.util.List;

/**
 * Camada de serviço responsável pelas regras de negócio dos medicamentos.
 */
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public void cadastrarMedicamento(Medicamento medicamento) {
        medicamentoRepository.salvar(medicamento);
    }

    public void atualizarMedicamento(Medicamento medicamento) {
        medicamentoRepository.atualizar(medicamento);
    }

    public List<Medicamento> listarTodos() {
        return medicamentoRepository.listarTodos();
    }
}
