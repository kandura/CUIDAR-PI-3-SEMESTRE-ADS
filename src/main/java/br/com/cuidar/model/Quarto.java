package br.com.cuidar.model;

/**
 * Representa um quarto da ILPI onde os residentes ficam alocados.
 */
public class Quarto {

    private int id;
    private int numero;
    private String status;

    public Quarto() {
    }

    public Quarto(int numero, String status) {
        this.numero = numero;
        this.status = status;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Quarto " + numero + " (" + status + ")";
    }
}
