package br.com.cuidar.model;

/**
 * Representa um médico externo vinculado à ILPI.
 * Referencia uma {@link Pessoa} para os dados pessoais.
 */
public class Medico {

    private int id;
    private Pessoa pessoa;
    private String crm;
    private String especialidade;
    private String telefone;
    private String email;

    public Medico() {
    }

    public Medico(Pessoa pessoa, String crm, String especialidade, String telefone, String email) {
        this.pessoa = pessoa;
        this.crm = crm;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.email = email;
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

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
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

    @Override
    public String toString() {
        return pessoa != null ? pessoa.getNomeCompleto() : "Médico " + id;
    }
}
