package me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.RotaDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Rota;
import org.mapstruct.Mapper;

@Mapper
public interface RotaDtoMapper {

    Rota dtoToDomain(RotaDto rotaDto);
    RotaDto domainToDto(Rota rota);

}
