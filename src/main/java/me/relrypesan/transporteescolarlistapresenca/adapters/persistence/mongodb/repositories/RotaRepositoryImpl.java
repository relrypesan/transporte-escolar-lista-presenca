package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.RotaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RotaRepositoryImpl extends MongoRepository<RotaEntity, String> {
    List<RotaEntity> findByUsuarioId(String idUsuario);
}
