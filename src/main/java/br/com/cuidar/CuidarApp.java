package br.com.cuidar;

import br.com.cuidar.model.Cargo;
import br.com.cuidar.model.Funcionario;
import br.com.cuidar.model.Medico;
import br.com.cuidar.model.Pessoa;
import br.com.cuidar.model.Quarto;
import br.com.cuidar.model.Residente;
import br.com.cuidar.model.ResidenteResponsavel;
import br.com.cuidar.model.Responsavel;

import java.time.LocalDate;

/**
 * Classe principal do sistema CUIDAR.
 * Versão 1.2 — adiciona os modelos de moradia (Quarto, Residente, Responsavel,
 * ResidenteResponsavel) sobre os modelos básicos já existentes em 1.1.
 */
public class CuidarApp {

    public static void main(String[] args) {
        System.out.println("=== Sistema CUIDAR ===");
        System.out.println("Versão 1.2 — Modelos de moradia\n");

        // Modelos da v1.1
        Cargo cuidador = new Cargo("Cuidador", "Cuidado direto aos residentes");
        cuidador.setId(2);

        Pessoa pFunc = new Pessoa("Ana Pereira", "222.333.444-55", "Feminino",
                LocalDate.of(1990, 8, 15));
        pFunc.setId(10);
        Funcionario func = new Funcionario(pFunc, cuidador, "ana.pereira", "secreta",
                "Tarde", "(11) 97777-2222", "ana@cuidar.com",
                "Av. Brasil", 200, "02020-000");
        func.setId(5);

        // Modelos novos da v1.2
        Quarto q101 = new Quarto(101, "Ocupado");
        q101.setId(1);

        Pessoa pResid = new Pessoa("José da Silva", "888.777.666-55", "Masculino",
                LocalDate.of(1940, 1, 5));
        pResid.setId(20);

        Residente residente = new Residente(pResid, q101, "Ativo",
                "Necessita auxílio para locomoção.");
        residente.setId(1);

        Pessoa pResp = new Pessoa("Carlos da Silva", "999.000.111-22", "Masculino",
                LocalDate.of(1972, 9, 25));
        pResp.setId(21);

        Responsavel responsavel = new Responsavel(pResp,
                "(11) 96666-3333", "carlos@email.com",
                "Rua das Acácias", 350, "Centro", "São Paulo", "SP", "03030-000");
        responsavel.setId(1);

        ResidenteResponsavel vinculo = new ResidenteResponsavel(residente, responsavel, "Filho");
        vinculo.setId(1);

        System.out.println(func);
        System.out.println(q101);
        System.out.println(residente);
        System.out.println(responsavel);
        System.out.println(vinculo);
    }
}
