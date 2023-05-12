package me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.MarcacaoDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import org.mapstruct.Mapper;

@Mapper
public interface MarcacaoDtoMapper {

    Marcacao dtoToDomain(MarcacaoDto marcacaoDto);
    MarcacaoDto domainToDto(Marcacao marcacao);

}
