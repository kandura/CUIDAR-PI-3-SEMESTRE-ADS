package br.com.cuidar.model;

/**
 * Representa um funcionário da ILPI.
 * Referencia uma {@link Pessoa} para os dados pessoais e um {@link Cargo} para a função.
 */
public class Funcionario {

    private int id;
    private Pessoa pessoa;
    private Cargo cargo;
    private String login;
    private String senha;
    private String turno;
    private String telefone;
    private String email;
    private String rua;
    private int numero;
    private String cep;

    public Funcionario() {
    }

    public Funcionario(Pessoa pessoa, Cargo cargo, String login, String senha, String turno,
                       String telefone, String email, String rua, int numero, String cep) {
        this.pessoa = pessoa;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
        this.turno = turno;
        this.telefone = telefone;
        this.email = email;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
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

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", pessoa=" + pessoa +
                ", cargo=" + cargo +
                ", login='" + login + '\'' +
                '}';
    }
}
