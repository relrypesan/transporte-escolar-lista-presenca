package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.AlunoEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.EscolaEntity;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import org.mapstruct.Mapper;

@Mapper
public interface AlunoMapper {

    Aluno entityToDomain(AlunoEntity alunoEntity);
    AlunoEntity domainToEntity(Aluno escola);

}
