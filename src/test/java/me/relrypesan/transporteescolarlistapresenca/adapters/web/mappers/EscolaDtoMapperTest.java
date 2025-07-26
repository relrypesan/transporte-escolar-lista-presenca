package me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.EscolaDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class EscolaDtoMapperTest {

    private final EscolaDtoMapper mapper = Mappers.getMapper(EscolaDtoMapper.class);

    @Test
    void dtoToDomain_ComDadosCompletos_DeveConverterCorretamente() {
        EscolaDto escolaDto = new EscolaDto("escola-id-1", "Escola Municipal João Silva", null);

        Escola resultado = mapper.dtoToDomain(escolaDto);

        assertNotNull(resultado);
        assertEquals(escolaDto.getId(), resultado.getId());
        assertEquals(escolaDto.getNome(), resultado.getNome());
    }

    @Test
    void domainToDto_ComDadosCompletos_DeveConverterCorretamente() {
        Escola escola = Escola.builder()
                .id("escola-id-1")
                .nome("Escola Municipal João Silva")
                .build();

        EscolaDto resultado = mapper.domainToDto(escola);

        assertNotNull(resultado);
        assertEquals(escola.getId(), resultado.getId());
        assertEquals(escola.getNome(), resultado.getNome());
    }

    @Test
    void dtoToDomain_ComDadosNulos_DeveRetornarNull() {
        Escola resultado = mapper.dtoToDomain(null);
        assertNull(resultado);
    }

    @Test
    void domainToDto_ComDadosNulos_DeveRetornarNull() {
        EscolaDto resultado = mapper.domainToDto(null);
        assertNull(resultado);
    }
}
