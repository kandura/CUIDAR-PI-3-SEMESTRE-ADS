package br.com.cuidar.model;

/**
 * Tabela associativa que vincula um {@link Residente} a um {@link Responsavel}.
 * Contém o grau de parentesco entre eles.
 */
public class ResidenteResponsavel {

    private int id;
    private Residente residente;
    private Responsavel responsavel;
    private String parentesco;

    public ResidenteResponsavel() {
    }

    public ResidenteResponsavel(Residente residente, Responsavel responsavel, String parentesco) {
        this.residente = residente;
        this.responsavel = responsavel;
        this.parentesco = parentesco;
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

    public Responsavel getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Responsavel responsavel) {
        this.responsavel = responsavel;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    @Override
    public String toString() {
        return "ResidenteResponsavel{" +
                "id=" + id +
                ", parentesco='" + parentesco + '\'' +
                '}';
    }
}
