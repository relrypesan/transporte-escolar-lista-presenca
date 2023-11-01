package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.RotaEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.UsuarioEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RotaRepositoryImpl extends MongoRepository<RotaEntity, String> {
}
