package br.com.cuidar.model;

import java.time.LocalDate;

/**
 * Representa um medicamento cadastrado no sistema.
 * Pode ser vinculado a registros clínicos dos residentes.
 */
public class Medicamento {

    private int id;
    private String nome;
    private String fabricante;
    private LocalDate dataValidade;
    private int quantidade;
    private String descricao;

    public Medicamento() {
    }

    public Medicamento(String nome, String fabricante, LocalDate dataValidade,
                       int quantidade, String descricao) {
        this.nome = nome;
        this.fabricante = fabricante;
        this.dataValidade = dataValidade;
        this.quantidade = quantidade;
        this.descricao = descricao;
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

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // MÉTODOS

    /**
     * Verifica se o medicamento está dentro da validade.
     *
     * @return - true se a data de validade for futura, false caso contrário
     */
    public boolean estaValido() {
        return dataValidade != null && dataValidade.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        return nome;
    }
}
