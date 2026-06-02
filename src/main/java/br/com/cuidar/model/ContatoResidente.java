package br.com.cuidar.model;

/**
 * Representa um contato responsável vinculado a um {@link Residente}.
 * Um residente pode ter zero ou mais contatos.
 */
public class ContatoResidente {

    private int id;
    private String nome;
    private String parentesco;
    private String telefone;
    private String email;
    private String observacoes;

    // RELACIONAMENTO 0..n:1 com Residente
    private Residente residente;

    public ContatoResidente() {
    }

    public ContatoResidente(int id, String nome, String parentesco, String telefone,
                            String email, String observacoes, Residente residente) {
        this.id = id;
        this.nome = nome;
        this.parentesco = parentesco;
        this.telefone = telefone;
        this.email = email;
        this.observacoes = observacoes;
        this.residente = residente;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Residente getResidente() {
        return residente;
    }

    public void setResidente(Residente residente) {
        this.residente = residente;
    }

    // MÉTODOS

    @Override
    public String toString() {
        return "ContatoResidente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", parentesco='" + parentesco + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
