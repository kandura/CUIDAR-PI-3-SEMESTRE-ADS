package br.com.cuidar.util;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Utilitário para aplicar máscaras e formatações automáticas em campos Swing.
 * Centraliza configurações de campos de data, hora, CPF, telefone, CEP e numéricos.
 */
public final class InputHelper {

    private InputHelper() {
    }

    /**
     * Aplica máscara de data dd/MM/yyyy com inserção automática de barras.
     * Aceita apenas dígitos; máximo 8 dígitos (10 caracteres com barras).
     */
    public static void aplicarMascaraData(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                                String text, AttributeSet attrs) throws BadLocationException {
                String digitsOnly = text.replaceAll("[^0-9]", "");
                if (digitsOnly.isEmpty()) return;

                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.replace(offset, offset + length, digitsOnly);
                String raw = sb.toString().replaceAll("[^0-9]", "");
                if (raw.length() > 8) return;

                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i == 2 || i == 4) formatted.append('/');
                    formatted.append(raw.charAt(i));
                }

                fb.getDocument().remove(0, fb.getDocument().getLength());
                super.insertString(fb, 0, formatted.toString(), attrs);
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                                     String text, AttributeSet attrs) throws BadLocationException {
                replace(fb, offset, 0, text, attrs);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    /**
     * Aplica máscara de hora HH:mm com inserção automática de dois-pontos.
     * Aceita apenas dígitos; máximo 4 dígitos (5 caracteres com ':').
     */
    public static void aplicarMascaraHora(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                                String text, AttributeSet attrs) throws BadLocationException {
                String digitsOnly = text.replaceAll("[^0-9]", "");
                if (digitsOnly.isEmpty()) return;

                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.replace(offset, offset + length, digitsOnly);
                String raw = sb.toString().replaceAll("[^0-9]", "");
                if (raw.length() > 4) return;

                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i == 2) formatted.append(':');
                    formatted.append(raw.charAt(i));
                }

                fb.getDocument().remove(0, fb.getDocument().getLength());
                super.insertString(fb, 0, formatted.toString(), attrs);
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                                     String text, AttributeSet attrs) throws BadLocationException {
                replace(fb, offset, 0, text, attrs);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    /**
     * Aplica máscara de CPF 000.000.000-00 com inserção automática de pontos e traço.
     * Aceita apenas dígitos; máximo 11 dígitos.
     */
    public static void aplicarMascaraCpf(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                                String text, AttributeSet attrs) throws BadLocationException {
                String digitsOnly = text.replaceAll("[^0-9]", "");
                if (digitsOnly.isEmpty()) return;

                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.replace(offset, offset + length, digitsOnly);
                String raw = sb.toString().replaceAll("[^0-9]", "");
                if (raw.length() > 11) return;

                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i == 3 || i == 6) formatted.append('.');
                    if (i == 9) formatted.append('-');
                    formatted.append(raw.charAt(i));
                }

                fb.getDocument().remove(0, fb.getDocument().getLength());
                super.insertString(fb, 0, formatted.toString(), attrs);
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                                     String text, AttributeSet attrs) throws BadLocationException {
                replace(fb, offset, 0, text, attrs);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    /**
     * Aplica máscara de telefone (00) 00000-0000 com inserção automática.
     * Aceita apenas dígitos; máximo 11 dígitos.
     */
    public static void aplicarMascaraTelefone(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                                String text, AttributeSet attrs) throws BadLocationException {
                String digitsOnly = text.replaceAll("[^0-9]", "");
                if (digitsOnly.isEmpty()) return;

                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.replace(offset, offset + length, digitsOnly);
                String raw = sb.toString().replaceAll("[^0-9]", "");
                if (raw.length() > 11) return;

                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i == 0) formatted.append('(');
                    if (i == 2) formatted.append(") ");
                    if (i == 7) formatted.append('-');
                    formatted.append(raw.charAt(i));
                }

                fb.getDocument().remove(0, fb.getDocument().getLength());
                super.insertString(fb, 0, formatted.toString(), attrs);
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                                     String text, AttributeSet attrs) throws BadLocationException {
                replace(fb, offset, 0, text, attrs);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    /**
     * Aplica máscara de CEP 00000-000 com inserção automática do traço.
     * Aceita apenas dígitos; máximo 8 dígitos.
     */
    public static void aplicarMascaraCep(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                                String text, AttributeSet attrs) throws BadLocationException {
                String digitsOnly = text.replaceAll("[^0-9]", "");
                if (digitsOnly.isEmpty()) return;

                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(current);
                sb.replace(offset, offset + length, digitsOnly);
                String raw = sb.toString().replaceAll("[^0-9]", "");
                if (raw.length() > 8) return;

                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i == 5) formatted.append('-');
                    formatted.append(raw.charAt(i));
                }

                fb.getDocument().remove(0, fb.getDocument().getLength());
                super.insertString(fb, 0, formatted.toString(), attrs);
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                                     String text, AttributeSet attrs) throws BadLocationException {
                replace(fb, offset, 0, text, attrs);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
            }
        });
    }

    /**
     * Restringe campo para aceitar apenas dígitos (inteiros).
     */
    public static void aplicarApenasNumeros(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                                String text, AttributeSet attrs) throws BadLocationException {
                String digitsOnly = text.replaceAll("[^0-9]", "");
                if (!digitsOnly.isEmpty()) {
                    super.replace(fb, offset, length, digitsOnly, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                                     String text, AttributeSet attrs) throws BadLocationException {
                replace(fb, offset, 0, text, attrs);
            }
        });
    }

    /**
     * Restringe campo para aceitar apenas dígitos e vírgula/ponto (decimais).
     */
    public static void aplicarApenasDecimal(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                                String text, AttributeSet attrs) throws BadLocationException {
                String filtered = text.replaceAll("[^0-9.,]", "");
                if (!filtered.isEmpty()) {
                    super.replace(fb, offset, length, filtered, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset,
                                     String text, AttributeSet attrs) throws BadLocationException {
                replace(fb, offset, 0, text, attrs);
            }
        });
    }
}
