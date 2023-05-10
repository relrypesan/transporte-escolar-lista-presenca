package me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.EscolaDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import org.mapstruct.Mapper;

@Mapper
public interface EscolaDtoMapper {

    Escola dtoToDomain(EscolaDto escolaDto);
    EscolaDto domainToDto(Escola escola);

}
