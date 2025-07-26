package me.relrypesan.transporteescolarlistapresenca.core.domain.service;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.AlunoEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.AlunoEntityMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.AlunoRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepositoryImpl alunoRepository;

    @Mock
    private AlunoEntityMapper alunoEntityMapper;

    @InjectMocks
    private AlunoService alunoService;

    private Aluno aluno;
    private AlunoEntity alunoEntity;

    @BeforeEach
    void setUp() {
        Escola escola = Escola.builder()
                .id("escola-id-1")
                .nome("Escola Teste")
                .build();

        aluno = Aluno.builder()
                .id("aluno-id-1")
                .nome("João Silva")
                .escola(escola)
                .build();

        alunoEntity = AlunoEntity.builder()
                .id("aluno-id-1")
                .nome("João Silva")
                .build();
    }

    @Test
    void cadastrarAluno_ComDadosValidos_DeveRetornarAlunoSalvo() {
        when(alunoEntityMapper.domainToEntity(any(Aluno.class))).thenReturn(alunoEntity);
        when(alunoRepository.save(any(AlunoEntity.class))).thenReturn(alunoEntity);
        when(alunoEntityMapper.entityToDomain(any(AlunoEntity.class))).thenReturn(aluno);

        Aluno resultado = alunoService.cadastrarAluno(aluno);

        assertNotNull(resultado);
        assertEquals(aluno.getId(), resultado.getId());
        assertEquals(aluno.getNome(), resultado.getNome());
        verify(alunoEntityMapper).domainToEntity(aluno);
        verify(alunoRepository).save(alunoEntity);
        verify(alunoEntityMapper).entityToDomain(alunoEntity);
    }

    @Test
    void paginarAlunos_SemFiltros_DeveRetornarPaginaCompleta() {
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, String> filters = new HashMap<>();
        Page<AlunoEntity> pageEntities = new PageImpl<>(Collections.singletonList(alunoEntity));

        when(alunoRepository.findAll(pageable)).thenReturn(pageEntities);
        when(alunoEntityMapper.entityToDomain(any(AlunoEntity.class))).thenReturn(aluno);

        Page<Aluno> resultado = alunoService.paginarAlunos(pageable, filters);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(aluno, resultado.getContent().get(0));
        verify(alunoRepository).findAll(pageable);
        verify(alunoEntityMapper).entityToDomain(alunoEntity);
    }

    @Test
    void paginarAlunos_ComFiltroEscola_DeveRetornarPaginaFiltrada() {
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, String> filters = new HashMap<>();
        filters.put("escola.id", "escola-id-1");
        Page<AlunoEntity> pageEntities = new PageImpl<>(Collections.singletonList(alunoEntity));

        when(alunoRepository.findByEscolaId(pageable, "escola-id-1")).thenReturn(pageEntities);
        when(alunoEntityMapper.entityToDomain(any(AlunoEntity.class))).thenReturn(aluno);

        Page<Aluno> resultado = alunoService.paginarAlunos(pageable, filters);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(alunoRepository).findByEscolaId(pageable, "escola-id-1");
        verify(alunoRepository, never()).findAll(pageable);
    }

    @Test
    void consultarAluno_ComIdExistente_DeveRetornarAluno() {
        when(alunoRepository.findById(anyString())).thenReturn(Optional.of(alunoEntity));
        when(alunoEntityMapper.entityToDomain(any(AlunoEntity.class))).thenReturn(aluno);

        Optional<Aluno> resultado = alunoService.consultarAluno("aluno-id-1");

        assertTrue(resultado.isPresent());
        assertEquals(aluno.getId(), resultado.get().getId());
        verify(alunoRepository).findById("aluno-id-1");
        verify(alunoEntityMapper).entityToDomain(alunoEntity);
    }

    @Test
    void consultarAluno_ComIdInexistente_DeveRetornarEmpty() {
        when(alunoRepository.findById(anyString())).thenReturn(Optional.empty());

        Optional<Aluno> resultado = alunoService.consultarAluno("id-inexistente");

        assertFalse(resultado.isPresent());
        verify(alunoRepository).findById("id-inexistente");
        verify(alunoEntityMapper, never()).entityToDomain(any());
    }

    @Test
    void atualizarAluno_ComIdValido_DeveRetornarAlunoAtualizado() {
        when(alunoRepository.findById(anyString())).thenReturn(Optional.of(alunoEntity));
        when(alunoEntityMapper.entityToDomain(any(AlunoEntity.class))).thenReturn(aluno);
        when(alunoEntityMapper.domainToEntity(any(Aluno.class))).thenReturn(alunoEntity);
        when(alunoRepository.save(any(AlunoEntity.class))).thenReturn(alunoEntity);

        Aluno resultado = alunoService.atualizarAluno(aluno);

        assertNotNull(resultado);
        assertEquals(aluno.getId(), resultado.getId());
        verify(alunoRepository).findById(aluno.getId());
        verify(alunoRepository).save(alunoEntity);
    }

    @Test
    void atualizarAluno_SemId_DeveLancarBusinessException() {
        Aluno alunoSemId = Aluno.builder()
                .nome("João Silva")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> alunoService.atualizarAluno(alunoSemId));

        assertEquals("ID aluno deve ser informado.", exception.getMessage());
        verify(alunoRepository, never()).findById(anyString());
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void atualizarAluno_ComIdInexistente_DeveLancarBusinessException() {
        when(alunoRepository.findById(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> alunoService.atualizarAluno(aluno));

        assertEquals("ID da aluno informado não foi encontrado: " + aluno.getId(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(alunoRepository).findById(aluno.getId());
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void deletarAluno_ComIdValido_DeveExecutarDelecao() {
        when(alunoRepository.findById(anyString())).thenReturn(Optional.of(alunoEntity));
        when(alunoEntityMapper.entityToDomain(any(AlunoEntity.class))).thenReturn(aluno);
        doNothing().when(alunoRepository).deleteById(anyString());

        assertDoesNotThrow(() -> alunoService.deletarAluno(aluno));

        verify(alunoRepository).findById(aluno.getId());
        verify(alunoRepository).deleteById(aluno.getId());
    }

    @Test
    void deletarAluno_SemId_DeveLancarBusinessException() {
        Aluno alunoSemId = Aluno.builder()
                .nome("João Silva")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> alunoService.deletarAluno(alunoSemId));

        assertEquals("ID aluno deve ser informado.", exception.getMessage());
        verify(alunoRepository, never()).findById(anyString());
        verify(alunoRepository, never()).deleteById(anyString());
    }

    @Test
    void deletarAluno_ComIdInexistente_DeveLancarBusinessException() {
        when(alunoRepository.findById(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> alunoService.deletarAluno(aluno));

        assertEquals("ID da aluno informado não foi encontrado: " + aluno.getId(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(alunoRepository).findById(aluno.getId());
        verify(alunoRepository, never()).deleteById(anyString());
    }
}
