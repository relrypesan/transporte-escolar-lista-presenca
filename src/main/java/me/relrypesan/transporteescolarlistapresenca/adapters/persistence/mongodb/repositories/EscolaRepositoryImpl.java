package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.EscolaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EscolaRepositoryImpl extends MongoRepository<EscolaEntity, String> {
}
