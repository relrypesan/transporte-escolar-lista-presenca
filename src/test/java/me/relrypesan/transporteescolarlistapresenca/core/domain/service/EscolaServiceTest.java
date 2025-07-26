package me.relrypesan.transporteescolarlistapresenca.core.domain.service;

import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.AlunoEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.EscolaEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.EscolaEntityMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.AlunoRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.EscolaRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EscolaServiceTest {

    @Mock
    private EscolaRepositoryImpl escolaRepository;

    @Mock
    private AlunoRepositoryImpl alunoRepository;

    @Mock
    private EscolaEntityMapper escolaEntityMapper;

    @InjectMocks
    private EscolaService escolaService;

    private Escola escola;
    private EscolaEntity escolaEntity;

    @BeforeEach
    void setUp() {
        escola = Escola.builder()
                .id("escola-id-1")
                .nome("Escola Municipal João Silva")
                .build();

        escolaEntity = EscolaEntity.builder()
                .id("escola-id-1")
                .nome("Escola Municipal João Silva")
                .build();
    }

    @Test
    void cadastrarEscola_ComDadosValidos_DeveRetornarEscolaSalva() {
        when(escolaEntityMapper.domainToEntity(any(Escola.class))).thenReturn(escolaEntity);
        when(escolaRepository.save(any(EscolaEntity.class))).thenReturn(escolaEntity);
        when(escolaEntityMapper.entityToDomain(any(EscolaEntity.class))).thenReturn(escola);

        Escola resultado = escolaService.cadastrarEscola(escola);

        assertNotNull(resultado);
        assertEquals(escola.getId(), resultado.getId());
        assertEquals(escola.getNome(), resultado.getNome());
        verify(escolaEntityMapper).domainToEntity(escola);
        verify(escolaRepository).save(escolaEntity);
        verify(escolaEntityMapper).entityToDomain(escolaEntity);
    }

    @Test
    void listarEscolas_DeveRetornarListaDeEscolas() {
        List<EscolaEntity> entities = Arrays.asList(escolaEntity, 
                EscolaEntity.builder().id("escola-id-2").nome("Escola Estadual Maria").build());
        
        when(escolaRepository.findAll()).thenReturn(entities);
        when(escolaEntityMapper.entityToDomain(any(EscolaEntity.class))).thenReturn(escola);

        List<Escola> resultado = escolaService.listarEscolas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(escolaRepository).findAll();
        verify(escolaEntityMapper, times(2)).entityToDomain(any(EscolaEntity.class));
    }

    @Test
    void consultarEscola_ComIdExistente_DeveRetornarEscola() {
        when(escolaRepository.findById(anyString())).thenReturn(Optional.of(escolaEntity));
        when(escolaEntityMapper.entityToDomain(any(EscolaEntity.class))).thenReturn(escola);

        Optional<Escola> resultado = escolaService.consultarEscola("escola-id-1");

        assertTrue(resultado.isPresent());
        assertEquals(escola.getId(), resultado.get().getId());
        verify(escolaRepository).findById("escola-id-1");
        verify(escolaEntityMapper).entityToDomain(escolaEntity);
    }

    @Test
    void consultarEscola_ComIdInexistente_DeveRetornarEmpty() {
        when(escolaRepository.findById(anyString())).thenReturn(Optional.empty());

        Optional<Escola> resultado = escolaService.consultarEscola("id-inexistente");

        assertFalse(resultado.isPresent());
        verify(escolaRepository).findById("id-inexistente");
        verify(escolaEntityMapper, never()).entityToDomain(any());
    }

    @Test
    void atualizarEscola_ComIdValido_DeveRetornarEscolaAtualizada() {
        when(escolaRepository.findById(anyString())).thenReturn(Optional.of(escolaEntity));
        when(escolaEntityMapper.entityToDomain(any(EscolaEntity.class))).thenReturn(escola);
        when(escolaEntityMapper.domainToEntity(any(Escola.class))).thenReturn(escolaEntity);
        when(escolaRepository.save(any(EscolaEntity.class))).thenReturn(escolaEntity);

        Escola resultado = escolaService.atualizarEscola(escola);

        assertNotNull(resultado);
        assertEquals(escola.getId(), resultado.getId());
        verify(escolaRepository).findById(escola.getId());
        verify(escolaRepository).save(escolaEntity);
    }

    @Test
    void atualizarEscola_SemId_DeveLancarBusinessException() {
        Escola escolaSemId = Escola.builder()
                .nome("Escola Teste")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> escolaService.atualizarEscola(escolaSemId));

        assertEquals("ID escola deve ser informado.", exception.getMessage());
        verify(escolaRepository, never()).findById(anyString());
        verify(escolaRepository, never()).save(any());
    }

    @Test
    void atualizarEscola_ComIdInexistente_DeveLancarBusinessException() {
        when(escolaRepository.findById(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> escolaService.atualizarEscola(escola));

        assertEquals("ID da escola informado não foi encontrado: " + escola.getId(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(escolaRepository).findById(escola.getId());
        verify(escolaRepository, never()).save(any());
    }

    @Test
    void deletarEscola_ComIdValidoSemAlunos_DeveExecutarDelecao() {
        when(escolaRepository.findById(anyString())).thenReturn(Optional.of(escolaEntity));
        when(escolaEntityMapper.entityToDomain(any(EscolaEntity.class))).thenReturn(escola);
        when(alunoRepository.findByEscolaId(anyString())).thenReturn(Collections.emptyList());
        doNothing().when(escolaRepository).deleteById(anyString());

        assertDoesNotThrow(() -> escolaService.deletarEscola(escola));

        verify(escolaRepository).findById(escola.getId());
        verify(alunoRepository).findByEscolaId(escola.getId());
        verify(escolaRepository).deleteById(escola.getId());
    }

    @Test
    void deletarEscola_ComAlunosVinculados_DeveLancarBusinessException() {
        List<AlunoEntity> alunosVinculados = Arrays.asList(
                AlunoEntity.builder().id("aluno-1").build(),
                AlunoEntity.builder().id("aluno-2").build()
        );

        when(escolaRepository.findById(anyString())).thenReturn(Optional.of(escolaEntity));
        when(escolaEntityMapper.entityToDomain(any(EscolaEntity.class))).thenReturn(escola);
        when(alunoRepository.findByEscolaId(anyString())).thenReturn(alunosVinculados);

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> escolaService.deletarEscola(escola));

        assertEquals("ID escola está sendo utilizado por registro de alunos", exception.getMessage());
        verify(escolaRepository).findById(escola.getId());
        verify(alunoRepository).findByEscolaId(escola.getId());
        verify(escolaRepository, never()).deleteById(anyString());
    }

    @Test
    void deletarEscola_SemId_DeveLancarBusinessException() {
        Escola escolaSemId = Escola.builder()
                .nome("Escola Teste")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> escolaService.deletarEscola(escolaSemId));

        assertEquals("ID escola deve ser informado.", exception.getMessage());
        verify(escolaRepository, never()).findById(anyString());
        verify(alunoRepository, never()).findByEscolaId(anyString());
        verify(escolaRepository, never()).deleteById(anyString());
    }

    @Test
    void deletarEscola_ComIdInexistente_DeveLancarBusinessException() {
        when(escolaRepository.findById(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> escolaService.deletarEscola(escola));

        assertEquals("ID da escola informado não foi encontrado: " + escola.getId(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(escolaRepository).findById(escola.getId());
        verify(alunoRepository, never()).findByEscolaId(anyString());
        verify(escolaRepository, never()).deleteById(anyString());
    }
}
