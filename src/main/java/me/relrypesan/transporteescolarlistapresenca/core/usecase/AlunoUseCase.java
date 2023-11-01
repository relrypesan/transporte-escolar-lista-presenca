package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.aluno.ConsultarAlunoPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.aluno.DeletarAlunoPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.aluno.SalvarAlunoPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.escola.ConsultarEscolaPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlunoUseCase {
    private final ConsultarAlunoPort consultarAlunoPort;
    private final SalvarAlunoPort salvarAlunoPort;
    private final DeletarAlunoPort deletarAlunoPort;
    private final ConsultarEscolaPort consultarEscola;

    public Aluno cadastrarNovoAluno(Aluno aluno) {
        if (aluno.getEscola() == null || aluno.getEscola().getId() == null) throw new BusinessException("Deve ser informado o ID escola para cadastrar o Aluno");

        var escolaOptional = consultarEscola.consultarEscola(aluno.getEscola().getId());
        if (escolaOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID escola não encontrado");

        return salvarAlunoPort.cadastrarAluno(aluno);
    }

    public Page<Aluno> listarAlunos(Pageable pageable, Map<String, String> filters) {
        return consultarAlunoPort.paginarAlunos(pageable, filters);
    }

    public Aluno consultarAluno(String idAluno) {
        var alunoOptional = consultarAlunoPort.consultarAluno(idAluno);
        if (alunoOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID aluno não encontrado");
        return alunoOptional.get();
    }

    public Aluno atualizarAluno(Aluno aluno) {
        if (aluno.getEscola() == null || aluno.getEscola().getId() == null) throw new BusinessException("Deve ser informado o ID escola para cadastrar o Aluno");

        var escolaOptional = consultarEscola.consultarEscola(aluno.getEscola().getId());
        if (escolaOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID escola não encontrado");

        return salvarAlunoPort.atualizarAluno(aluno);
    }

    public void deletarAluno(String idAluno) {
        deletarAlunoPort.deletarAluno(idAluno);
    }

}
