package br.com.cuidar.util;

/**
 * Utilitário para validação de CPF usando o algoritmo dos dígitos verificadores.
 */
public final class CpfUtil {

    private CpfUtil() {
    }

    /**
     * Valida um CPF usando o cálculo dos dois dígitos verificadores.
     *
     * @param cpf CPF com ou sem pontuação
     * @return true se o CPF é válido
     */
    public static boolean isValid(String cpf) {
        if (cpf == null) return false;

        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) return false;

        if (cpf.chars().distinct().count() == 1) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;

        if ((cpf.charAt(9) - '0') != digito1) return false;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;

        return (cpf.charAt(10) - '0') == digito2;
    }
}
