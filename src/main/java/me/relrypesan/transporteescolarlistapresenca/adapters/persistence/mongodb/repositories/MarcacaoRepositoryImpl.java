package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.MarcacaoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface MarcacaoRepositoryImpl extends MongoRepository<MarcacaoEntity, String> {
    List<MarcacaoEntity> findByAlunoId(String idAluno);
    Page<MarcacaoEntity> findByAlunoId(Pageable pageable, String idAluno);
    Page<MarcacaoEntity> findByAlunoIdAndDataMarcacaoEquals(Pageable pageable, String idAluno, LocalDate dataMarcacao);
}
