package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.UsuarioEntity;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Usuario;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface UsuarioEntityMapper {

    @Mappings({
            @Mapping(target = "pessoa.nome", source = "nome"),
            @Mapping(target = "pessoa.sexo", source = "sexo"),
            @Mapping(target = "pessoa.dataNascimento", source = "dataNascimento"),
            @Mapping(target = "pessoa.endereco", source = "endereco"),
            @Mapping(target = "pessoa.telefones", source = "telefones")
    })
    UsuarioEntity domainToEntity(Usuario usuario);

    @InheritInverseConfiguration
    Usuario entityToDomain(UsuarioEntity usuarioEntity);
}
