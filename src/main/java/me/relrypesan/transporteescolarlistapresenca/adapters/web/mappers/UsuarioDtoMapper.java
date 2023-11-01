package me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.UsuarioDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UsuarioDtoMapper {

    Usuario dtoToDomain(UsuarioDto usuarioDto);

    @Mapping(target = "password", ignore = true)
    UsuarioDto domainToDto(Usuario usuario);

}
