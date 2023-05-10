package me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.AlunoDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import org.mapstruct.Mapper;

@Mapper
public interface AlunoDtoMapper {

    Aluno dtoToDomain(AlunoDto alunoDto);
    AlunoDto domainToDto(Aluno escola);

}
