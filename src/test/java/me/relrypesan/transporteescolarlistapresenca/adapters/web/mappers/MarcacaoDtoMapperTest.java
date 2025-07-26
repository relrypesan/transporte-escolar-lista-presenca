package me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.AlunoDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.MarcacaoDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.TipoMarcacaoEnum;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MarcacaoDtoMapperTest {

    private final MarcacaoDtoMapper mapper = Mappers.getMapper(MarcacaoDtoMapper.class);

    @Test
    void dtoToDomain_ComDadosCompletos_DeveConverterCorretamente() {
        AlunoDto alunoDto = new AlunoDto("aluno-id-1", "João Silva", null, null, null, null, null, null, null);

        MarcacaoDto marcacaoDto = new MarcacaoDto("marcacao-id-1", LocalDate.of(2023, 12, 25), 
                LocalTime.of(8, 30), TipoMarcacaoEnum.ENTRADA, alunoDto, "Observação teste");

        Marcacao resultado = mapper.dtoToDomain(marcacaoDto);

        assertNotNull(resultado);
        assertEquals(marcacaoDto.getId(), resultado.getId());
        assertEquals(marcacaoDto.getDataMarcacao(), resultado.getDataMarcacao());
        assertEquals(marcacaoDto.getHoraMarcacao(), resultado.getHoraMarcacao());
        assertEquals(marcacaoDto.getTipoMarcacao(), resultado.getTipoMarcacao());
        assertEquals(marcacaoDto.getObservacao(), resultado.getObservacao());
        assertNotNull(resultado.getAluno());
        assertEquals(alunoDto.getId(), resultado.getAluno().getId());
        assertEquals(alunoDto.getNome(), resultado.getAluno().getNome());
    }

    @Test
    void domainToDto_ComDadosCompletos_DeveConverterCorretamente() {
        Aluno aluno = Aluno.builder()
                .id("aluno-id-1")
                .nome("João Silva")
                .build();

        Marcacao marcacao = Marcacao.builder()
                .id("marcacao-id-1")
                .dataMarcacao(LocalDate.of(2023, 12, 25))
                .horaMarcacao(LocalTime.of(8, 30))
                .tipoMarcacao(TipoMarcacaoEnum.ENTRADA)
                .aluno(aluno)
                .observacao("Observação teste")
                .build();

        MarcacaoDto resultado = mapper.domainToDto(marcacao);

        assertNotNull(resultado);
        assertEquals(marcacao.getId(), resultado.getId());
        assertEquals(marcacao.getDataMarcacao(), resultado.getDataMarcacao());
        assertEquals(marcacao.getHoraMarcacao(), resultado.getHoraMarcacao());
        assertEquals(marcacao.getTipoMarcacao(), resultado.getTipoMarcacao());
        assertEquals(marcacao.getObservacao(), resultado.getObservacao());
        assertNotNull(resultado.getAluno());
        assertEquals(aluno.getId(), resultado.getAluno().getId());
        assertEquals(aluno.getNome(), resultado.getAluno().getNome());
    }

    @Test
    void dtoToDomain_ComDadosNulos_DeveRetornarNull() {
        Marcacao resultado = mapper.dtoToDomain(null);
        assertNull(resultado);
    }

    @Test
    void domainToDto_ComDadosNulos_DeveRetornarNull() {
        MarcacaoDto resultado = mapper.domainToDto(null);
        assertNull(resultado);
    }
}
