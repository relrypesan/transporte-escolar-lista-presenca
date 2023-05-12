package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.MarcacaoEntity;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import org.mapstruct.Mapper;

@Mapper
public interface MarcacaoEntityMapper {

    Marcacao entityToDomain(MarcacaoEntity marcacaoEntity);
    MarcacaoEntity domainToEntity(Marcacao marcacao);

}
