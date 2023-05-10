package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.AlunoEntity;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import org.mapstruct.Mapper;

@Mapper
public interface AlunoMapper {

    Aluno entityToDomain(AlunoEntity alunoEntity);
    AlunoEntity domainToEntity(Aluno escola);

}
