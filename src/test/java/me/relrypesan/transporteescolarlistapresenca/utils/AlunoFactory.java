package me.relrypesan.transporteescolarlistapresenca.utils;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Endereco;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.SexoEnum;

import java.time.LocalDate;

public class AlunoFactory {

    public static Aluno criarAluno() {
        return Aluno.builder()
                .id("aluno-1234")
                .nome("Aluno Fulano 1")
                .sexo(SexoEnum.MASCULINO)
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .endereco(Endereco.builder()
                        .id("endereco-aluno-1234")
                        .build())
                .escola(EscolaFactory.criarEscola())
                .build();
    }

}
