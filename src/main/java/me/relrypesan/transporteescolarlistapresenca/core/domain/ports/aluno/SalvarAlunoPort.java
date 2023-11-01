package me.relrypesan.transporteescolarlistapresenca.core.domain.ports.aluno;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;

public interface SalvarAlunoPort {
    Aluno cadastrarAluno(Aluno aluno);
    Aluno atualizarAluno(Aluno aluno);
}
