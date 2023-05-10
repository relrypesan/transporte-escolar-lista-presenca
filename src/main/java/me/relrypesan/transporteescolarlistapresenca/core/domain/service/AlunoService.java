package me.relrypesan.transporteescolarlistapresenca.core.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.AlunoMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.AlunoRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.EscolaRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepositoryImpl alunoRepository;
    private final EscolaRepositoryImpl escolaRepository;
    private final AlunoMapper alunoMapper;

    public Aluno cadastrarAluno(Aluno escola) {
        var entity = alunoMapper.domainToEntity(escola);
        if (entity.getEscola() != null) {
            var escolaEntityOptional = escolaRepository.findById(entity.getEscola().getId());

        }
        entity = alunoRepository.save(entity);
        return alunoMapper.entityToDomain(entity);
    }

    public List<Aluno> listarAlunos() {
        var entities = alunoRepository.findAll();
        var listaAluno = entities.stream()
                .map(alunoMapper::entityToDomain)
                .collect(Collectors.toList());
        return listaAluno;
    }

    public Optional<Aluno> consultarAluno(String idAluno) {
        var entities = alunoRepository.findById(idAluno);
        return entities.map(alunoMapper::entityToDomain);
    }

    public Aluno atualizarAluno(Aluno aluno) {
        if (aluno.getId() == null) throw new BusinessException("ID aluno deve ser informado.");

        var escolaConsultada = consultarAluno(aluno.getId());
        if (escolaConsultada.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID da aluno informado não foi encontrado: " + aluno.getId());

        return cadastrarAluno(aluno);
    }

    public void deletarAluno(Aluno aluno) {
        if (aluno.getId() == null) throw new BusinessException("ID aluno deve ser informado.");

        var escolaConsultada = consultarAluno(aluno.getId());
        if (escolaConsultada.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID da aluno informado não foi encontrado: " + aluno.getId());

        alunoRepository.deleteById(aluno.getId());
    }

}
