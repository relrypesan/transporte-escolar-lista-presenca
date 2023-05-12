package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import lombok.RequiredArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.AlunoService;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.EscolaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AlunoUseCase {
    private final AlunoService alunoService;
    private final EscolaService escolaService;

    public Aluno cadastrarNovoAluno(Aluno aluno) {
        if (aluno.getEscola() == null || aluno.getEscola().getId() == null) throw new BusinessException("Deve ser informado o ID escola para cadastrar o Aluno");

        var escolaOptional = escolaService.consultarEscola(aluno.getEscola().getId());
        if (escolaOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID escola não encontrado");

        return alunoService.cadastrarAluno(aluno);
    }

    public Page<Aluno> listarAlunos(Pageable pageable, Map<String, String> filters) {
        return alunoService.paginarAlunos(pageable, filters);
    }

    public Aluno consultarAluno(String idAluno) {
        var alunoOptional = alunoService.consultarAluno(idAluno);
        if (alunoOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID aluno não encontrado");
        return alunoOptional.get();
    }

    public Aluno atualizarAluno(Aluno aluno) {
        if (aluno.getEscola() == null || aluno.getEscola().getId() == null) throw new BusinessException("Deve ser informado o ID escola para cadastrar o Aluno");

        var escolaOptional = escolaService.consultarEscola(aluno.getEscola().getId());
        if (escolaOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID escola não encontrado");

        return alunoService.atualizarAluno(aluno);
    }

    public void deletarAluno(String idAluno) {
        alunoService.deletarAluno(Aluno.builder()
                .id(idAluno)
                .build());
    }

}
