package me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.UsuarioDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Usuario;
import org.mapstruct.Mapper;

@Mapper
public interface UsuarioDtoMapper {

    Usuario dtoToDomain(UsuarioDto usuarioDto);
    UsuarioDto domainToDto(Usuario usuario);

}
