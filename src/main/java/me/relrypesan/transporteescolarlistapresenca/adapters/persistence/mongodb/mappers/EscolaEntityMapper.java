package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.EscolaEntity;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import org.mapstruct.Mapper;

@Mapper
public interface EscolaEntityMapper {

    Escola entityToDomain(EscolaEntity escolaEntity);
    EscolaEntity domainToEntity(Escola escola);

}
