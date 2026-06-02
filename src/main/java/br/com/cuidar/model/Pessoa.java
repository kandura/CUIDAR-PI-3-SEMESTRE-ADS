package br.com.cuidar.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Classe padrão para a criação de pessoas.
 */
public class Pessoa {
	private final int IDADE_MIN = 18;
	private final int IDADE_MAX = 120;

	private String cpf;
	private String nomeCompleto;
	private LocalDate dataNascimento;
	private boolean ativo;
	private String genero;

	public Pessoa() {
	}

	public Pessoa(String cpf, String nomeCompleto, LocalDate dataNascimento, String genero, boolean ativo) {
		setCpf(cpf);
		setNomeCompleto(nomeCompleto);
		setDataNascimento(dataNascimento);
		setGenero(genero);
		this.ativo = ativo;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		if (!validarCpf(cpf)) {
			throw new IllegalArgumentException("CPF inválido: " + cpf);
		}
		this.cpf = cpf;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		if (!validaNome(nomeCompleto)) {
			throw new IllegalArgumentException("Nome inválido: " + nomeCompleto);
		}
		this.nomeCompleto = nomeCompleto;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		if (!validarDataNascimento(dataNascimento, IDADE_MIN, IDADE_MAX)) {
			throw new IllegalArgumentException("Data de Nascimento inválida: " + dataNascimento);
		}
		this.dataNascimento = dataNascimento;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * Inativa uma pessoa.
	 */
	public void inativarPessoa() {
		if (this.ativo) {
			this.ativo = false;
		}
	}

	/**
	 * Ativa uma pessoa.
	 */
	public void ativarPessoa() {
		if (!this.ativo) {
			this.ativo = true;
		}
	}

	/**
	 * Valida o genero, se está de acordo com o definido no enum, para não gerar
	 * generos inexistentes.
	 * 
	 * @param genero - paramentro passado com o genero escolhido
	 * @return retorna true caso o genero esteja dentro dos aceitos pelo enum
	 */
	public static boolean validarGenero(String genero) {
		boolean r = false;
		for (Genero g : Genero.values()) {
			if (g.name().equalsIgnoreCase(genero)) {
				r = true;
			}
		}
		return r;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		if (!validarGenero(genero)) {
			throw new IllegalArgumentException("Genero inválido: " + genero);
		}
		this.genero = genero;
	}

	/**
	 * Valida um CPF usando o algoritmo oficial da Receita Federal. Aceita CPF
	 * formatado (ex: "123.456.789-09") ou apenas números ("12345678909").
	 * 
	 * @param cpf - String com o CPF a ser validado
	 * @return - true se o CPF for válido, false caso contrário
	 */
	public boolean validarCpf(String cpf) {
		// Verifica se há alguma coisa escrita no campo.
		if (cpf == null)
			return false;

		// Remove formatação: pontos e traço
		cpf = cpf.replaceAll("[.\\-]", "").trim();

		// Verifica se tem exatamente 11 dígitos numéricos
		if (!cpf.matches("\\d{11}"))
			return false;

		// Rejeita CPFs com todos os dígitos iguais (ex: 111.111.111-11)
		if (cpf.chars().distinct().count() == 1)
			return false;

		// Calcula e valida o 1º dígito verificador
		int primeiroDigito = calcularDigito(cpf, 9);
		if (primeiroDigito != Character.getNumericValue(cpf.charAt(9)))
			return false;

		// Calcula e valida o 2º dígito verificador
		int segundoDigito = calcularDigito(cpf, 10);
		if (segundoDigito != Character.getNumericValue(cpf.charAt(10)))
			return false;

		return true;
	}

	/**
	 * Calcula um dígito verificador do CPF.
	 *
	 * @param cpf    - String com os 11 dígitos do CPF
	 * @param length Quantos dígitos usar no cálculo (9 para o 1º, 10 para o 2º)
	 * @return O dígito verificador calculado
	 */
	private static int calcularDigito(String cpf, int tamanho) {
		int soma = 0;
		int peso = tamanho + 1;

		for (int i = 0; i < tamanho; i++) {
			soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
		}

		int resto = soma % 11;
		return (resto < 2) ? 0 : (11 - resto);
	}

	/**
	 * Valida se o nome é valido, se ele não está em branco ou se não há numeros
	 * nele.
	 * 
	 * @param nome - Variavel passada para verificação
	 * @return - true se o Nome for válido, false caso contrário
	 */
	public static boolean validaNome(String nome) {
		if (nome == null || nome.isEmpty()) {
			return false;
		}
		for (int i = 0; i < nome.length(); i++) {
			if (Character.isDigit(nome.charAt(i))) {
				return false; // Encontrou um número, retorna false
			}
		}
		return true; // Percorreu tudo e não achou números
	}

	/**
	 * Valida se a data de nascimento é valida, se ela for uma data futura ou uma
	 * data muito grande(insinuando que a pessoa tenha mais de 120 anos)
	 * 
	 * @param data     - data recebida para a validação
	 * @param idadeMin - idade minima definida por uma variavel final
	 * @param idadeMax - idade maxima definida por uma variavel final
	 * @return - retorna false caso esteja diferente da regra de negocio. True caso
	 *         contrario.
	 */
	public static boolean validarDataNascimento(LocalDate dataNascimento, int idadeMin, int idadeMax) {
		if (dataNascimento == null) {
			return false;
		}

		LocalDate hoje = LocalDate.now();

		// Não pode ser data futura
		if (dataNascimento.isAfter(hoje)) {
			return false;
		}

		// Calcula a idade em anos, se for menor que 18, não é possivel cadastro
		int idade = Period.between(dataNascimento, hoje).getYears();
		if (idade < idadeMin || idade > idadeMax) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		return "Pessoa [cpf=" + cpf + ", nomeCompleto=" + nomeCompleto + ", dataNascimento=" + dataNascimento
				+ ", genero=" + genero + ", ativo=" + ativo + "]";
	}

}
