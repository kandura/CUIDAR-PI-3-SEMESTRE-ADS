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

    /**
     * Cadastra um novo medicamento no sistema.
     *
     * @param medicamento - medicamento a ser cadastrado
     */
    public void cadastrarMedicamento(Medicamento medicamento) {
        medicamentoRepository.salvar(medicamento);
    }

    /**
     * Atualiza os dados de um medicamento existente.
     *
     * @param medicamento - medicamento com os dados atualizados
     */
    public void atualizarMedicamento(Medicamento medicamento) {
        medicamentoRepository.atualizar(medicamento);
    }

    /**
     * Lista todos os medicamentos cadastrados.
     *
     * @return - lista de medicamentos
     */
    public List<Medicamento> listarTodos() {
        return medicamentoRepository.listarTodos();
    }
}
