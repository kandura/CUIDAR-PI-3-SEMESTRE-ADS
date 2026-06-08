package br.com.cuidar.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representa uma pessoa no sistema.
 * Entidade base referenciada por Residente, Funcionario, Responsavel e Medico.
 */
public class Pessoa {

    private int id;
    private String nomeCompleto;
    private String cpf;
    private String sexo;
    private LocalDate dataNascimento;
    private LocalDateTime dataCadastro;

    public Pessoa() {
    }

    public Pessoa(String nomeCompleto, String cpf, String sexo, LocalDate dataNascimento) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
