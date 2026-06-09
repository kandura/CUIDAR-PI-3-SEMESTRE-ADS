package br.com.cuidar;

import br.com.cuidar.model.Residente;
import br.com.cuidar.model.ResidenteResponsavel;
import br.com.cuidar.model.Responsavel;
import br.com.cuidar.repository.ResidenteRepository;
import br.com.cuidar.repository.ResidenteResponsavelRepository;
import br.com.cuidar.repository.ResponsavelRepository;
import br.com.cuidar.repository.impl.ResidenteRepositoryImpl;
import br.com.cuidar.repository.impl.ResidenteResponsavelRepositoryImpl;
import br.com.cuidar.repository.impl.ResponsavelRepositoryImpl;

import java.util.List;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 2.5 — adiciona as implementações JDBC de {@code ResidenteRepositoryImpl},
 * {@code ResponsavelRepositoryImpl} e {@code ResidenteResponsavelRepositoryImpl}
 * (associativa entre Residente e Responsavel com grau de parentesco).
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 2.5 — Repositórios JDBC: Residente, Responsavel, ResidenteResponsavel\n");

        ResidenteRepository resRepo = new ResidenteRepositoryImpl();
        ResponsavelRepository respRepo = new ResponsavelRepositoryImpl();
        ResidenteResponsavelRepository rrRepo = new ResidenteResponsavelRepositoryImpl();

        try {
            List<Residente> residentes = resRepo.listarTodos();
            System.out.println("Residentes cadastrados (" + residentes.size() + "):");
            for (Residente r : residentes) {
                System.out.println("  - " + r.getId() + " | " + r.getPessoa().getNomeCompleto()
                        + " | Quarto " + r.getQuarto().getNumero()
                        + " | status=" + r.getStatus());
            }

            List<Responsavel> responsaveis = respRepo.listarTodos();
            System.out.println("\nResponsáveis cadastrados (" + responsaveis.size() + "):");
            for (Responsavel resp : responsaveis) {
                System.out.println("  - " + resp.getId() + " | " + resp.getPessoa().getNomeCompleto()
                        + " | " + resp.getCidade() + "/" + resp.getEstado()
                        + " | tel=" + resp.getTelefone());
            }

            if (!residentes.isEmpty()) {
                Residente r = residentes.get(0);
                List<ResidenteResponsavel> vinculos = rrRepo.listarPorResidente(r);
                System.out.println("\nResponsáveis do residente '" + r.getPessoa().getNomeCompleto()
                        + "' (" + vinculos.size() + "):");
                for (ResidenteResponsavel v : vinculos) {
                    System.out.println("  - " + v.getResponsavel().getPessoa().getNomeCompleto()
                            + " (" + v.getParentesco() + ")");
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Falha ao consultar o banco: " + e.getMessage());
        }
    }
}
