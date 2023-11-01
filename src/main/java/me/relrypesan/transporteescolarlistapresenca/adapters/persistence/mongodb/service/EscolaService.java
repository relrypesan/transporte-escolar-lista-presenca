package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.EscolaEntityMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.AlunoRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.EscolaRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.escola.ConsultarEscolaPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.escola.DeletarEscolaPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.escola.SalvarEscolaPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EscolaService implements ConsultarEscolaPort, SalvarEscolaPort, DeletarEscolaPort {

    private final EscolaRepositoryImpl escolaRepository;
    private final AlunoRepositoryImpl alunoRepository;
    private final EscolaEntityMapper escolaEntityMapper;

    public Escola cadastrarNovaEscola(Escola escola) {
        var entity = escolaEntityMapper.domainToEntity(escola);
        entity = escolaRepository.save(entity);
        return escolaEntityMapper.entityToDomain(entity);
    }

    public List<Escola> listarTodasEscola() {
        var entities = escolaRepository.findAll();
        return entities.stream()
                .map(escolaEntityMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    public Optional<Escola> consultarEscola(String idEscola) {
        var entities = escolaRepository.findById(idEscola);
        return entities.map(escolaEntityMapper::entityToDomain);
    }

    public Escola atualizarEscola(Escola escola) {
        if (escola.getId() == null) throw new BusinessException("ID escola deve ser informado.");

        var escolaConsultada = consultarEscola(escola.getId());
        if (escolaConsultada.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID da escola informado não foi encontrado: " + escola.getId());

        return cadastrarNovaEscola(escola);
    }

    public void deletarEscola(String escola) {
        if (escola == null) throw new BusinessException("ID escola deve ser informado.");

        var escolaConsultada = consultarEscola(escola);
        if (escolaConsultada.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND,"ID da escola informado não foi encontrado: " + escola);

        var listaAlunos = alunoRepository.findByEscolaId(escola);
        if (!listaAlunos.isEmpty()) throw new BusinessException("ID escola está sendo utilizado por registro de alunos");

        escolaRepository.deleteById(escola);
    }
}
