package me.relrypesan.transporteescolarlistapresenca.core.domain.service;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.MarcacaoEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.MarcacaoEntityMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.MarcacaoRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.TipoMarcacaoEnum;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarcacaoServiceTest {

    @Mock
    private MarcacaoRepositoryImpl marcacaoRepository;

    @Mock
    private MarcacaoEntityMapper marcacaoEntityMapper;

    @InjectMocks
    private MarcacaoService marcacaoService;

    private Marcacao marcacao;
    private MarcacaoEntity marcacaoEntity;
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = Aluno.builder()
                .id("aluno-id-1")
                .nome("João Silva")
                .build();

        marcacao = Marcacao.builder()
                .id("marcacao-id-1")
                .dataMarcacao(LocalDate.now())
                .horaMarcacao(LocalTime.now())
                .tipoMarcacao(TipoMarcacaoEnum.ENTRADA)
                .aluno(aluno)
                .build();

        marcacaoEntity = MarcacaoEntity.builder()
                .id("marcacao-id-1")
                .dataMarcacao(LocalDate.now())
                .horaMarcacao(LocalTime.now())
                .tipoMarcacao(TipoMarcacaoEnum.ENTRADA)
                .build();
    }

    @Test
    void cadastrarMarcacao_ComDadosValidos_DeveRetornarMarcacaoSalva() {
        when(marcacaoEntityMapper.domainToEntity(any(Marcacao.class))).thenReturn(marcacaoEntity);
        when(marcacaoRepository.save(any(MarcacaoEntity.class))).thenReturn(marcacaoEntity);
        when(marcacaoEntityMapper.entityToDomain(any(MarcacaoEntity.class))).thenReturn(marcacao);

        Marcacao resultado = marcacaoService.cadastrarMarcacao(marcacao);

        assertNotNull(resultado);
        assertEquals(marcacao.getId(), resultado.getId());
        assertEquals(marcacao.getTipoMarcacao(), resultado.getTipoMarcacao());
        verify(marcacaoEntityMapper).domainToEntity(marcacao);
        verify(marcacaoRepository).save(marcacaoEntity);
        verify(marcacaoEntityMapper).entityToDomain(marcacaoEntity);
    }

    @Test
    void paginarMarcacao_SemFiltros_DeveRetornarPaginaCompleta() {
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, String> filters = new HashMap<>();
        String idAluno = "aluno-id-1";
        Page<MarcacaoEntity> pageEntities = new PageImpl<>(Collections.singletonList(marcacaoEntity));

        when(marcacaoRepository.findByAlunoId(pageable, idAluno)).thenReturn(pageEntities);
        when(marcacaoEntityMapper.entityToDomain(any(MarcacaoEntity.class))).thenReturn(marcacao);

        Page<Marcacao> resultado = marcacaoService.paginarMarcacao(pageable, filters, idAluno);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(marcacao, resultado.getContent().get(0));
        verify(marcacaoRepository).findByAlunoId(pageable, idAluno);
        verify(marcacaoEntityMapper).entityToDomain(marcacaoEntity);
    }

    @Test
    void paginarMarcacao_ComFiltroData_DeveRetornarPaginaFiltrada() {
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, String> filters = new HashMap<>();
        LocalDate dataFiltro = LocalDate.of(2023, 12, 25);
        filters.put("data_marcacao", dataFiltro.toString());
        String idAluno = "aluno-id-1";
        Page<MarcacaoEntity> pageEntities = new PageImpl<>(Collections.singletonList(marcacaoEntity));

        when(marcacaoRepository.findByAlunoIdAndDataMarcacaoEquals(pageable, idAluno, dataFiltro))
                .thenReturn(pageEntities);
        when(marcacaoEntityMapper.entityToDomain(any(MarcacaoEntity.class))).thenReturn(marcacao);

        Page<Marcacao> resultado = marcacaoService.paginarMarcacao(pageable, filters, idAluno);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(marcacaoRepository).findByAlunoIdAndDataMarcacaoEquals(pageable, idAluno, dataFiltro);
        verify(marcacaoRepository, never()).findByAlunoId(any(), anyString());
    }

    @Test
    void consultarMarcacao_ComIdExistente_DeveRetornarMarcacao() {
        when(marcacaoRepository.findById(anyString())).thenReturn(Optional.of(marcacaoEntity));
        when(marcacaoEntityMapper.entityToDomain(any(MarcacaoEntity.class))).thenReturn(marcacao);

        Optional<Marcacao> resultado = marcacaoService.consultarMarcacao("marcacao-id-1");

        assertTrue(resultado.isPresent());
        assertEquals(marcacao.getId(), resultado.get().getId());
        verify(marcacaoRepository).findById("marcacao-id-1");
        verify(marcacaoEntityMapper).entityToDomain(marcacaoEntity);
    }

    @Test
    void consultarMarcacao_ComIdInexistente_DeveRetornarEmpty() {
        when(marcacaoRepository.findById(anyString())).thenReturn(Optional.empty());

        Optional<Marcacao> resultado = marcacaoService.consultarMarcacao("id-inexistente");

        assertFalse(resultado.isPresent());
        verify(marcacaoRepository).findById("id-inexistente");
        verify(marcacaoEntityMapper, never()).entityToDomain(any());
    }

    @Test
    void atualizarMarcacao_ComIdValido_DeveRetornarMarcacaoAtualizada() {
        when(marcacaoRepository.findById(anyString())).thenReturn(Optional.of(marcacaoEntity));
        when(marcacaoEntityMapper.entityToDomain(any(MarcacaoEntity.class))).thenReturn(marcacao);
        when(marcacaoEntityMapper.domainToEntity(any(Marcacao.class))).thenReturn(marcacaoEntity);
        when(marcacaoRepository.save(any(MarcacaoEntity.class))).thenReturn(marcacaoEntity);

        Marcacao resultado = marcacaoService.atualizarMarcacao(marcacao);

        assertNotNull(resultado);
        assertEquals(marcacao.getId(), resultado.getId());
        verify(marcacaoRepository).findById(marcacao.getId());
        verify(marcacaoRepository).save(marcacaoEntity);
    }

    @Test
    void atualizarMarcacao_SemId_DeveLancarBusinessException() {
        Marcacao marcacaoSemId = Marcacao.builder()
                .dataMarcacao(LocalDate.now())
                .tipoMarcacao(TipoMarcacaoEnum.ENTRADA)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> marcacaoService.atualizarMarcacao(marcacaoSemId));

        assertEquals("ID marcação deve ser informado.", exception.getMessage());
        verify(marcacaoRepository, never()).findById(anyString());
        verify(marcacaoRepository, never()).save(any());
    }

    @Test
    void atualizarMarcacao_ComIdInexistente_DeveLancarBusinessException() {
        when(marcacaoRepository.findById(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> marcacaoService.atualizarMarcacao(marcacao));

        assertEquals("ID da marcacao informado não foi encontrado: " + marcacao.getId(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(marcacaoRepository).findById(marcacao.getId());
        verify(marcacaoRepository, never()).save(any());
    }

    @Test
    void deletarMarcacao_ComIdValido_DeveExecutarDelecao() {
        when(marcacaoRepository.findById(anyString())).thenReturn(Optional.of(marcacaoEntity));
        when(marcacaoEntityMapper.entityToDomain(any(MarcacaoEntity.class))).thenReturn(marcacao);
        doNothing().when(marcacaoRepository).deleteById(anyString());

        assertDoesNotThrow(() -> marcacaoService.deletarMarcacao(marcacao));

        verify(marcacaoRepository).findById(marcacao.getId());
        verify(marcacaoRepository).deleteById(marcacao.getId());
    }

    @Test
    void deletarMarcacao_SemId_DeveLancarBusinessException() {
        Marcacao marcacaoSemId = Marcacao.builder()
                .dataMarcacao(LocalDate.now())
                .tipoMarcacao(TipoMarcacaoEnum.ENTRADA)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> marcacaoService.deletarMarcacao(marcacaoSemId));

        assertEquals("ID marcação deve ser informado.", exception.getMessage());
        verify(marcacaoRepository, never()).findById(anyString());
        verify(marcacaoRepository, never()).deleteById(anyString());
    }

    @Test
    void deletarMarcacao_ComIdInexistente_DeveLancarBusinessException() {
        when(marcacaoRepository.findById(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> marcacaoService.deletarMarcacao(marcacao));

        assertEquals("ID da marcação informado não foi encontrado: " + marcacao.getId(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(marcacaoRepository).findById(marcacao.getId());
        verify(marcacaoRepository, never()).deleteById(anyString());
    }
}
