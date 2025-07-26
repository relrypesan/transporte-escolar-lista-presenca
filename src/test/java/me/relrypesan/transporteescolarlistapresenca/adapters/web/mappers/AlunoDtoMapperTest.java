package me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers;

import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.AlunoDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.EscolaDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.SexoEnum;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AlunoDtoMapperTest {

    private final AlunoDtoMapper mapper = Mappers.getMapper(AlunoDtoMapper.class);

    @Test
    void dtoToDomain_ComDadosCompletos_DeveConverterCorretamente() {
        EscolaDto escolaDto = new EscolaDto("escola-id-1", "Escola Teste", null);

        AlunoDto alunoDto = new AlunoDto("aluno-id-1", "João Silva", SexoEnum.MASCULINO, 
                LocalDate.of(2010, 5, 15), null, escolaDto, null, "QR123456", "Observação teste");

        Aluno resultado = mapper.dtoToDomain(alunoDto);

        assertNotNull(resultado);
        assertEquals(alunoDto.getId(), resultado.getId());
        assertEquals(alunoDto.getNome(), resultado.getNome());
        assertEquals(alunoDto.getSexo(), resultado.getSexo());
        assertEquals(alunoDto.getDataNascimento(), resultado.getDataNascimento());
        assertEquals(alunoDto.getQrCode(), resultado.getQrCode());
        assertEquals(alunoDto.getObservacao(), resultado.getObservacao());
        assertNotNull(resultado.getEscola());
        assertEquals(escolaDto.getId(), resultado.getEscola().getId());
        assertEquals(escolaDto.getNome(), resultado.getEscola().getNome());
    }

    @Test
    void domainToDto_ComDadosCompletos_DeveConverterCorretamente() {
        Escola escola = Escola.builder()
                .id("escola-id-1")
                .nome("Escola Teste")
                .build();

        Aluno aluno = Aluno.builder()
                .id("aluno-id-1")
                .nome("João Silva")
                .sexo(SexoEnum.MASCULINO)
                .dataNascimento(LocalDate.of(2010, 5, 15))
                .escola(escola)
                .qrCode("QR123456")
                .observacao("Observação teste")
                .build();

        AlunoDto resultado = mapper.domainToDto(aluno);

        assertNotNull(resultado);
        assertEquals(aluno.getId(), resultado.getId());
        assertEquals(aluno.getNome(), resultado.getNome());
        assertEquals(aluno.getSexo(), resultado.getSexo());
        assertEquals(aluno.getDataNascimento(), resultado.getDataNascimento());
        assertEquals(aluno.getQrCode(), resultado.getQrCode());
        assertEquals(aluno.getObservacao(), resultado.getObservacao());
        assertNotNull(resultado.getEscola());
        assertEquals(escola.getId(), resultado.getEscola().getId());
        assertEquals(escola.getNome(), resultado.getEscola().getNome());
    }

    @Test
    void dtoToDomain_ComDadosNulos_DeveRetornarNull() {
        Aluno resultado = mapper.dtoToDomain(null);
        assertNull(resultado);
    }

    @Test
    void domainToDto_ComDadosNulos_DeveRetornarNull() {
        AlunoDto resultado = mapper.domainToDto(null);
        assertNull(resultado);
    }
}
