package me.relrypesan.transporteescolarlistapresenca.core.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.MarcacaoEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.MarcacaoEntityMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.MarcacaoRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarcacaoService {

    private final MarcacaoRepositoryImpl marcacaoRepository;
    private final MarcacaoEntityMapper marcacaoEntityMapper;

    public Marcacao cadastrarMarcacao(Marcacao marcacao) {
        var entity = marcacaoEntityMapper.domainToEntity(marcacao);
        entity = marcacaoRepository.save(entity);
        return marcacaoEntityMapper.entityToDomain(entity);
    }

    public List<Marcacao> listarMarcacoes() {
        var entities = marcacaoRepository.findAll();
        var listaMarcacoes = entities.stream()
                .map(marcacaoEntityMapper::entityToDomain)
                .collect(Collectors.toList());
        return listaMarcacoes;
    }

    public Page<Marcacao> paginarMarcacao(Pageable pageable, Map<String, String> filters, String idAluno) {
        Page<MarcacaoEntity> pageEntities;
        if (filters.containsKey("data_marcacao")) {
            pageEntities = marcacaoRepository.findByAlunoIdAndDataMarcacaoEquals(pageable, idAluno, LocalDate.parse(filters.get("data_marcacao")));
        } else {
            pageEntities = marcacaoRepository.findByAlunoId(pageable, idAluno);
        }
        return pageEntities.map(marcacaoEntityMapper::entityToDomain);
    }

    public Optional<Marcacao> consultarMarcacao(String idMarcacao) {
        var entities = marcacaoRepository.findById(idMarcacao);
        return entities.map(marcacaoEntityMapper::entityToDomain);
    }

    public Marcacao atualizarMarcacao(Marcacao marcacao) {
        if (marcacao.getId() == null) throw new BusinessException("ID marcação deve ser informado.");

        var marcacaoOptional = consultarMarcacao(marcacao.getId());
        if (marcacaoOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID da marcacao informado não foi encontrado: " + marcacao.getId());

        return cadastrarMarcacao(marcacao);
    }

    public void deletarMarcacao(Marcacao marcacao) {
        if (marcacao.getId() == null) throw new BusinessException("ID marcação deve ser informado.");

        var marcacaoConsultada = consultarMarcacao(marcacao.getId());
        if (marcacaoConsultada.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND,"ID da marcação informado não foi encontrado: " + marcacao.getId());

        marcacaoRepository.deleteById(marcacao.getId());
    }
}
