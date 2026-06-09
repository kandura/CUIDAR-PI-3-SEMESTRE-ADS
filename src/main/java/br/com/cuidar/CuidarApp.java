package br.com.cuidar;

import br.com.cuidar.model.Atividade;
import br.com.cuidar.model.Medicamento;
import br.com.cuidar.model.Prontuario;
import br.com.cuidar.model.RegistroClinico;
import br.com.cuidar.model.Residente;
import br.com.cuidar.repository.AtividadeRepository;
import br.com.cuidar.repository.MedicamentoRepository;
import br.com.cuidar.repository.ProntuarioRepository;
import br.com.cuidar.repository.RegistroClinicoRepository;
import br.com.cuidar.repository.ResidenteRepository;
import br.com.cuidar.repository.impl.AtividadeRepositoryImpl;
import br.com.cuidar.repository.impl.MedicamentoRepositoryImpl;
import br.com.cuidar.repository.impl.ProntuarioRepositoryImpl;
import br.com.cuidar.repository.impl.RegistroClinicoRepositoryImpl;
import br.com.cuidar.repository.impl.ResidenteRepositoryImpl;

import java.util.List;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 2.6 — fecha o conjunto de 12 implementações JDBC adicionando
 * {@code ProntuarioRepositoryImpl}, {@code MedicamentoRepositoryImpl},
 * {@code RegistroClinicoRepositoryImpl} e {@code AtividadeRepositoryImpl}.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 2.6 — Repositórios JDBC: Prontuario, Medicamento, RegistroClinico, Atividade\n");

        ResidenteRepository resRepo = new ResidenteRepositoryImpl();
        ProntuarioRepository pronRepo = new ProntuarioRepositoryImpl();
        MedicamentoRepository medRepo = new MedicamentoRepositoryImpl();
        RegistroClinicoRepository rcRepo = new RegistroClinicoRepositoryImpl();
        AtividadeRepository atvRepo = new AtividadeRepositoryImpl();

        try {
            List<Medicamento> medicamentos = medRepo.listarTodos();
            System.out.println("Catálogo de medicamentos (" + medicamentos.size() + "):");
            for (Medicamento m : medicamentos) {
                System.out.println("  - " + m.getId() + " | " + m.getNome()
                        + " (" + m.getFabricante() + ")"
                        + " | qtd=" + m.getQuantidade()
                        + " | val=" + m.getDataValidade());
            }

            List<Atividade> atividades = atvRepo.listarTodos();
            System.out.println("\nAtividades cadastradas (" + atividades.size() + "):");
            for (Atividade a : atividades) {
                System.out.println("  - " + a.getDiaSemana() + " " + a.getHoraInicio()
                        + "-" + a.getHoraTermino() + " | " + a.getNome());
            }

            List<Residente> residentes = resRepo.listarTodos();
            if (!residentes.isEmpty()) {
                Residente r = residentes.get(0);

                Prontuario pron = pronRepo.buscarPorResidente(r);
                System.out.println("\nProntuário de '" + r.getPessoa().getNomeCompleto() + "':");
                if (pron == null) {
                    System.out.println("  (sem prontuário cadastrado)");
                } else {
                    System.out.println("  peso=" + pron.getPeso() + " | altura=" + pron.getAltura()
                            + " | tipo=" + pron.getTipoSanguineo()
                            + " | alergias=" + pron.getAlergias());
                }

                List<RegistroClinico> registros = rcRepo.listarPorResidente(r);
                System.out.println("\nRegistros clínicos de '" + r.getPessoa().getNomeCompleto()
                        + "' (" + registros.size() + "):");
                for (RegistroClinico rc : registros) {
                    System.out.println("  - " + rc.getDataRegistro() + " | " + rc.getTipoEvento()
                            + " | med=" + rc.getMedicamento().getNome()
                            + " | dose=" + rc.getDosagem()
                            + " | func=" + rc.getFuncionario().getPessoa().getNomeCompleto());
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Falha ao consultar o banco: " + e.getMessage());
        }
    }
}
