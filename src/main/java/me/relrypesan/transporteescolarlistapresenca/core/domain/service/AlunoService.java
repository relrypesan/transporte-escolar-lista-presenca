package me.relrypesan.transporteescolarlistapresenca.core.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.AlunoEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.AlunoEntityMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.AlunoRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepositoryImpl alunoRepository;
    private final AlunoEntityMapper alunoEntityMapper;

    public Aluno cadastrarAluno(Aluno escola) {
        var entity = alunoEntityMapper.domainToEntity(escola);
        entity = alunoRepository.save(entity);
        return alunoEntityMapper.entityToDomain(entity);
    }

    public Page<Aluno> paginarAlunos(Pageable pageable, Map<String, String> filters) {
        Page<AlunoEntity> pageEntities;
        if (filters.containsKey("escola.id")) {
            pageEntities = alunoRepository.findByEscolaId(pageable, filters.get("escola.id"));
        } else {
            pageEntities = alunoRepository.findAll(pageable);
        }
        return pageEntities.map(alunoEntityMapper::entityToDomain);
    }

    public Optional<Aluno> consultarAluno(String idAluno) {
        var entities = alunoRepository.findById(idAluno);
        return entities.map(alunoEntityMapper::entityToDomain);
    }

    public Aluno atualizarAluno(Aluno aluno) {
        if (aluno.getId() == null) throw new BusinessException("ID aluno deve ser informado.");

        var alunoConsultado = consultarAluno(aluno.getId());
        if (alunoConsultado.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID da aluno informado não foi encontrado: " + aluno.getId());

        return cadastrarAluno(aluno);
    }

    public void deletarAluno(Aluno aluno) {
        if (aluno.getId() == null) throw new BusinessException("ID aluno deve ser informado.");

        var escolaConsultada = consultarAluno(aluno.getId());
        if (escolaConsultada.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID da aluno informado não foi encontrado: " + aluno.getId());

        alunoRepository.deleteById(aluno.getId());
    }

}
