package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import lombok.RequiredArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlunoUseCase {
    private final AlunoService alunoService;

    public Aluno cadastrarNovoAluno(Aluno aluno) {
        return alunoService.cadastrarAluno(aluno);
    }

    public List<Aluno> listarTodasEscola() {
        return alunoService.listarAlunos();
    }

    public Aluno consultarEscola(String idAluno) {
        var alunoOptional = alunoService.consultarAluno(idAluno);
        if (alunoOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID aluno não encontrado");
        return alunoOptional.get();
    }

    public Aluno atualizarEscola(Aluno aluno) {
        var escolaAtualizada = alunoService.atualizarAluno(aluno);
        return escolaAtualizada;
    }

    public void deletarEscola(String idAluno) {
        alunoService.deletarAluno(Aluno.builder()
                .id(idAluno)
                .build());
    }

}
