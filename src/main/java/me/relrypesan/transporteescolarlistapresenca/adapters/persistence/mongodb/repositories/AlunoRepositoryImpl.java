package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.AlunoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlunoRepositoryImpl extends MongoRepository<AlunoEntity, String> {
}
