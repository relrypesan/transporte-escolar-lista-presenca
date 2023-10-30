package me.relrypesan.transporteescolarlistapresenca.core.domain.ports.aluno;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface ConsultarAlunoPort {
    Optional<Aluno> consultarAluno(String idAluno);
    Page<Aluno> paginarAlunos(Pageable pageable, Map<String, String> filters);
}
