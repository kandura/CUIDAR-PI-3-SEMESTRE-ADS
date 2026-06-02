package br.com.cuidar.model;

import java.time.LocalDate;

/**
 * Representa o prontuário médico de um {@link Residente}.
 * Contém informações clínicas fixas como doenças crônicas, alergias e tipo sanguíneo.
 * Relacionamento 1:1 com Residente.
 */
public class Prontuario {

    private int id;
    private String doencas;
    private String alergias;
    private String tipoSanguineo;
    private String condicoesMedicasRelevantes;
    private LocalDate dataCriacao;

    // RELACIONAMENTO 1:1 com Residente
    private Residente residente;

    public Prontuario() {
    }

    public Prontuario(int id, String doencas, String alergias, String tipoSanguineo,
                      String condicoesMedicasRelevantes, LocalDate dataCriacao, Residente residente) {
        this.id = id;
        this.doencas = doencas;
        this.alergias = alergias;
        this.tipoSanguineo = tipoSanguineo;
        this.condicoesMedicasRelevantes = condicoesMedicasRelevantes;
        this.dataCriacao = dataCriacao;
        this.residente = residente;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoencas() {
        return doencas;
    }

    public void setDoencas(String doencas) {
        this.doencas = doencas;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public String getCondicoesMedicasRelevantes() {
        return condicoesMedicasRelevantes;
    }

    public void setCondicoesMedicasRelevantes(String condicoesMedicasRelevantes) {
        this.condicoesMedicasRelevantes = condicoesMedicasRelevantes;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Residente getResidente() {
        return residente;
    }

    public void setResidente(Residente residente) {
        this.residente = residente;
    }

    // MÉTODOS

    /**
     * Verifica se o residente possui alguma alergia registrada no prontuário.
     *
     * @return - true se houver alergias cadastradas, false caso contrário
     */
    public boolean possuiAlergia() {
        return alergias != null && !alergias.isEmpty();
    }

    /**
     * Verifica se o residente possui doenças crônicas registradas no prontuário.
     *
     * @return - true se houver doenças crônicas cadastradas, false caso contrário
     */
    public boolean possuiDoencasCronicas() {
        return doencas != null && !doencas.isEmpty();
    }

    @Override
    public String toString() {
        return "Prontuario{" +
                "id=" + id +
                ", doencas='" + doencas + '\'' +
                ", alergias='" + alergias + '\'' +
                ", tipoSanguineo='" + tipoSanguineo + '\'' +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}
