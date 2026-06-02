package br.com.cuidar.model;

import java.time.LocalDate;
import java.time.Period;

public class Residente extends Pessoa {

	private LocalDate dataEntradaInstituicao;
	private String telefone;
	private String observacoesGerais;

	public Residente() {
	}

	public Residente(String cpf, String nomeCompleto, LocalDate dataNascimento, String genero, boolean ativo,
			String telefone, String observacoesGerais) {
		super(cpf, nomeCompleto, dataNascimento, genero, ativo);
		this.dataEntradaInstituicao = LocalDate.now();
		this.telefone = telefone;
		this.observacoesGerais = observacoesGerais;
	}

	public LocalDate getDataEntradaInstituicao() {
		return dataEntradaInstituicao;
	}

	public void setDataEntradaInstituicao(LocalDate dataEntradaInstituicao) {
		this.dataEntradaInstituicao = dataEntradaInstituicao;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getObservacoesGerais() {
		return observacoesGerais;
	}

	public void setObservacoesGerais(String observacoesGerais) {
		this.observacoesGerais = observacoesGerais;
	}
	/**
	 * Calcula há quanto tempo o residente está na instituição
	 * 
	 * @param dataEntradaInstituicao - Data de entrada do residente na instituição
	 * @return - Valor em anos de quantos anos ele está na instituição
	 */
	public int consultaTempoResidente(LocalDate dataEntradaInstituicao) {
		int idade = Period.between(dataEntradaInstituicao, LocalDate.now()).getYears();
		return idade;
	}

	@Override
	public String toString() {
		return "Residente [dataEntradaInstituicao=" + dataEntradaInstituicao + ", telefone=" + telefone
				+ ", observacoesGerais=" + observacoesGerais + "]";
	}
	
}
