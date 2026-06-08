package br.com.cuidar.model;

import java.time.LocalTime;

/**
 * Representa uma atividade da programação semanal da ILPI.
 */
public class Atividade {

    private int id;
    private String nome;
    private String descricao;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaTermino;

    public Atividade() {
    }

    public Atividade(String nome, String descricao, String diaSemana,
                     LocalTime horaInicio, LocalTime horaTermino) {
        this.nome = nome;
        this.descricao = descricao;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaTermino = horaTermino;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(LocalTime horaTermino) {
        this.horaTermino = horaTermino;
    }

    @Override
    public String toString() {
        return "Atividade{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", diaSemana='" + diaSemana + '\'' +
                ", horaInicio=" + horaInicio +
                ", horaTermino=" + horaTermino +
                '}';
    }
}
