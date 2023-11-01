package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.RotaEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.UsuarioEntity;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Rota;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Usuario;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface RotaEntityMapper {

    RotaEntity domainToEntity(Rota rota);

    @InheritInverseConfiguration
    Rota entityToDomain(RotaEntity rotaEntity);
}
