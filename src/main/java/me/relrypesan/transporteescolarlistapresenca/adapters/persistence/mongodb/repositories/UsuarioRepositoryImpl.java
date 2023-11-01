package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.UsuarioEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepositoryImpl extends MongoRepository<UsuarioEntity, String> {
    Optional<UsuarioEntity> findByUsername(String username);
}
