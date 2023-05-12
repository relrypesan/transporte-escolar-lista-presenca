package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.AlunoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlunoRepositoryImpl extends MongoRepository<AlunoEntity, String> {
    List<AlunoEntity> findByEscolaId(String idEscola);
    Page<AlunoEntity> findByEscolaId(Pageable pageable, String idEscola);
}
