package br.com.cuidar.model;

/**
 * Representa um residente da ILPI.
 * Referencia uma {@link Pessoa} para os dados pessoais e um {@link Quarto} para alocação.
 */
public class Residente {

    private int id;
    private Pessoa pessoa;
    private Quarto quarto;
    private String status;
    private String obsGeral;

    public Residente() {
    }

    public Residente(Pessoa pessoa, Quarto quarto, String status, String obsGeral) {
        this.pessoa = pessoa;
        this.quarto = quarto;
        this.status = status;
        this.obsGeral = obsGeral;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObsGeral() {
        return obsGeral;
    }

    public void setObsGeral(String obsGeral) {
        this.obsGeral = obsGeral;
    }

    @Override
    public String toString() {
        return "Residente{" +
                "id=" + id +
                ", pessoa=" + pessoa +
                ", quarto=" + quarto +
                ", status='" + status + '\'' +
                '}';
    }
}