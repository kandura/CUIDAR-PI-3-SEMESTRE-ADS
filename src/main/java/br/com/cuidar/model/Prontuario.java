package br.com.cuidar.model;

/**
 * Representa o prontuário médico de um {@link Residente}.
 * Contém informações clínicas fixas como peso, altura, tipo sanguíneo e alergias.
 * Relacionamento 1:1 com Residente.
 */
public class Prontuario {

    private int id;
    private Residente residente;
    private double peso;
    private double altura;
    private String tipoSanguineo;
    private String alergias;
    private String obsGeral;

    public Prontuario() {
    }

    public Prontuario(Residente residente, double peso, double altura,
                      String tipoSanguineo, String alergias, String obsGeral) {
        this.residente = residente;
        this.peso = peso;
        this.altura = altura;
        this.tipoSanguineo = tipoSanguineo;
        this.alergias = alergias;
        this.obsGeral = obsGeral;
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

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getObsGeral() {
        return obsGeral;
    }

    public void setObsGeral(String obsGeral) {
        this.obsGeral = obsGeral;
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

    @Override
    public String toString() {
        return "Prontuario{" +
                "id=" + id +
                ", peso=" + peso +
                ", altura=" + altura +
                ", tipoSanguineo='" + tipoSanguineo + '\'' +
                ", alergias='" + alergias + '\'' +
                '}';
    }
}
