package br.com.cuidar.model;

/**
 * Representa um cargo que pode ser atribuído a um {@link Funcionario} na ILPI.
 */
public class Cargo {

    private int id;
    private String nomeCargo;
    private String descricao;

    public Cargo() {
    }

    public Cargo(String nomeCargo, String descricao) {
        this.nomeCargo = nomeCargo;
        this.descricao = descricao;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCargo() {
        return nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "id=" + id +
                ", nomeCargo='" + nomeCargo + '\'' +
                '}';
    }
}
