package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.RotaEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.RotaEntityMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.RotaRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Rota;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.rota.ConsultarRotaPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.rota.DeletarRotaPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.rota.SalvarRotaPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RotaService implements ConsultarRotaPort, SalvarRotaPort, DeletarRotaPort {

    private final RotaRepositoryImpl rotaRepository;
    private final RotaEntityMapper rotaEntityMapper;

    @Override
    public Rota cadastrar(Rota rota) {
        var entity = rotaEntityMapper.domainToEntity(rota);
        entity = rotaRepository.save(entity);
        return rotaEntityMapper.entityToDomain(entity);
    }

    @Override
    public Page<Rota> paginar(Pageable pageable, Map<String, String> filters) {
        Page<RotaEntity> pageEntities = rotaRepository.findAll(pageable);
        return pageEntities.map(rotaEntityMapper::entityToDomain);
    }

    @Override
    public Optional<Rota> consultar(String idRota) {
        var entities = rotaRepository.findById(idRota);
        return entities.map(rotaEntityMapper::entityToDomain);
    }

    @Override
    public List<Rota> consultarRotasPorUsuario(String idUsuario) {
        List<RotaEntity> entities = rotaRepository.findByUsuarioId(idUsuario);
        return entities.stream().map(rotaEntityMapper::entityToDomain).collect(Collectors.toList());
    }

    @Override
    public Rota atualizar(Rota rota) {
        if (rota.getId() == null) throw new BusinessException("ID rota deve ser informado.");

        var usuarioConsultado = consultar(rota.getId());
        if (usuarioConsultado.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID da rota informado não foi encontrado: " + rota.getId());

        return cadastrar(rota);
    }

    @Override
    public void deletar(Rota rota) {

    }

}
