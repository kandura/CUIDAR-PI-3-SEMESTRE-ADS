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
