package br.com.cuidar.model;

import java.time.LocalDate;

/**
 * Representa um registro clínico na evolução de saúde de um {@link Residente}.
 * Vinculado ao {@link Funcionario} que registrou, ao {@link Medico} que prescreveu
 * e ao {@link Medicamento} utilizado.
 */
public class RegistroClinico {

    private int id;
    private Residente residente;
    private Funcionario funcionario;
    private Medicamento medicamento;
    private Medico medico;
    private String tipoEvento;
    private String intercorrencia;
    private LocalDate dataRegistro;
    private String dosagem;

    public RegistroClinico() {
    }

    public RegistroClinico(Residente residente, Funcionario funcionario, Medicamento medicamento,
                           Medico medico, String tipoEvento, String intercorrencia,
                           LocalDate dataRegistro, String dosagem) {
        this.residente = residente;
        this.funcionario = funcionario;
        this.medicamento = medicamento;
        this.medico = medico;
        this.tipoEvento = tipoEvento;
        this.intercorrencia = intercorrencia;
        this.dataRegistro = dataRegistro;
        this.dosagem = dosagem;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Residente getResidente() {
        return residente;
    }

    public void setResidente(Residente residente) {
        this.residente = residente;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getIntercorrencia() {
        return intercorrencia;
    }

    public void setIntercorrencia(String intercorrencia) {
        this.intercorrencia = intercorrencia;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    // MÉTODOS

    /**
     * Verifica se houve intercorrência neste registro clínico.
     *
     * @return - true se houver intercorrência registrada, false caso contrário
     */
    public boolean possuiIntercorrencia() {
        return intercorrencia != null && !intercorrencia.isEmpty();
    }

    @Override
    public String toString() {
        return "RegistroClinico{" +
                "id=" + id +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", dataRegistro=" + dataRegistro +
                ", intercorrencia='" + intercorrencia + '\'' +
                '}';
    }
}