package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.AlunoEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.ResponsavelEntity;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Responsavel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface AlunoMapper {

    @Mappings({
            @Mapping(target = "pessoa.nome", source = "nome"),
            @Mapping(target = "pessoa.sexo", source = "sexo"),
            @Mapping(target = "pessoa.dataNascimento", source = "dataNascimento"),
            @Mapping(target = "pessoa.endereco", source = "endereco"),
            @Mapping(target = "pessoa.telefones", source = "telefones"),
    })
    ResponsavelEntity map(Responsavel responsavel);

    @Mappings({
            @Mapping(target = "nome", source = "pessoa.nome"),
            @Mapping(target = "sexo", source = "pessoa.sexo"),
            @Mapping(target = "dataNascimento", source = "pessoa.dataNascimento"),
            @Mapping(target = "endereco", source = "pessoa.endereco"),
            @Mapping(target = "telefones", source = "pessoa.telefones"),
    })
    Responsavel map(ResponsavelEntity responsavelEntity);

    Aluno entityToDomain(AlunoEntity alunoEntity);
    AlunoEntity domainToEntity(Aluno escola);

}
