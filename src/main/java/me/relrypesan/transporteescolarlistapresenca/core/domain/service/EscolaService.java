package me.relrypesan.transporteescolarlistapresenca.core.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.EscolaMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.AlunoRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.EscolaRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EscolaService {

    private final EscolaRepositoryImpl escolaRepository;
    private final AlunoRepositoryImpl alunoRepository;
    private final EscolaMapper escolaMapper;

    public Escola cadastrarEscola(Escola escola) {
        var entity = escolaMapper.domainToEntity(escola);
        entity = escolaRepository.save(entity);
        return escolaMapper.entityToDomain(entity);
    }

    public List<Escola> listarEscolas() {
        var entities = escolaRepository.findAll();
        var listaEscolar = entities.stream()
                .map(escolaMapper::entityToDomain)
                .collect(Collectors.toList());
        return listaEscolar;
    }

    public Optional<Escola> consultarEscola(String idEscola) {
        var entities = escolaRepository.findById(idEscola);
        return entities.map(escolaMapper::entityToDomain);
    }

    public Escola atualizarEscola(Escola escola) {
        if (escola.getId() == null) throw new BusinessException("ID escola deve ser informado.");

        var escolaConsultada = consultarEscola(escola.getId());
        if (escolaConsultada.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID da escola informado não foi encontrado: " + escola.getId());

        return cadastrarEscola(escola);
    }

    public void deletarEscola(Escola escola) {
        if (escola.getId() == null) throw new BusinessException("ID escola deve ser informado.");

        var escolaConsultada = consultarEscola(escola.getId());
        if (escolaConsultada.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND,"ID da escola informado não foi encontrado: " + escola.getId());

        var listaAlunos = alunoRepository.findByEscolaId(escola.getId());
        if (!listaAlunos.isEmpty()) throw new BusinessException("ID escola está sendo utilizado por registro de alunos");

        escolaRepository.deleteById(escola.getId());
    }
}
