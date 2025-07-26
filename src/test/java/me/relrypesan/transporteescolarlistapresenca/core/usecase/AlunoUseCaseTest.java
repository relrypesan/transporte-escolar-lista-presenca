package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.AlunoService;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.EscolaService;
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
class AlunoUseCaseTest {

    @Mock
    private AlunoService alunoService;

    @Mock
    private EscolaService escolaService;

    @InjectMocks
    private AlunoUseCase alunoUseCase;

    private Aluno aluno;
    private Escola escola;

    @BeforeEach
    void setUp() {
        escola = Escola.builder()
                .id("escola-id-1")
                .nome("Escola Teste")
                .build();

        aluno = Aluno.builder()
                .id("aluno-id-1")
                .nome("João Silva")
                .escola(escola)
                .build();
    }

    @Test
    void cadastrarNovoAluno_ComEscolaValida_DeveRetornarAlunoSalvo() {
        when(escolaService.consultarEscola(anyString())).thenReturn(Optional.of(escola));
        when(alunoService.cadastrarAluno(any(Aluno.class))).thenReturn(aluno);

        Aluno resultado = alunoUseCase.cadastrarNovoAluno(aluno);

        assertNotNull(resultado);
        assertEquals(aluno.getId(), resultado.getId());
        assertEquals(aluno.getNome(), resultado.getNome());
        verify(escolaService).consultarEscola(escola.getId());
        verify(alunoService).cadastrarAluno(aluno);
    }

    @Test
    void cadastrarNovoAluno_SemEscola_DeveLancarBusinessException() {
        Aluno alunoSemEscola = Aluno.builder()
                .nome("João Silva")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> alunoUseCase.cadastrarNovoAluno(alunoSemEscola));

        assertEquals("Deve ser informado o ID escola para cadastrar o Aluno", exception.getMessage());
        verify(escolaService, never()).consultarEscola(anyString());
        verify(alunoService, never()).cadastrarAluno(any());
    }

    @Test
    void cadastrarNovoAluno_ComEscolaInexistente_DeveLancarBusinessException() {
        when(escolaService.consultarEscola(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> alunoUseCase.cadastrarNovoAluno(aluno));

        assertEquals("ID escola não encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(escolaService).consultarEscola(escola.getId());
        verify(alunoService, never()).cadastrarAluno(any());
    }

    @Test
    void listarAlunos_DeveRetornarPaginaDeAlunos() {
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, String> filters = new HashMap<>();
        Page<Aluno> pageAlunos = new PageImpl<>(Collections.singletonList(aluno));

        when(alunoService.paginarAlunos(pageable, filters)).thenReturn(pageAlunos);

        Page<Aluno> resultado = alunoUseCase.listarAlunos(pageable, filters);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(aluno, resultado.getContent().get(0));
        verify(alunoService).paginarAlunos(pageable, filters);
    }

    @Test
    void consultarAluno_ComIdValido_DeveRetornarAluno() {
        when(alunoService.consultarAluno(anyString())).thenReturn(Optional.of(aluno));

        Aluno resultado = alunoUseCase.consultarAluno("aluno-id-1");

        assertNotNull(resultado);
        assertEquals(aluno.getId(), resultado.getId());
        assertEquals(aluno.getNome(), resultado.getNome());
        verify(alunoService).consultarAluno("aluno-id-1");
    }

    @Test
    void consultarAluno_ComIdInexistente_DeveLancarBusinessException() {
        when(alunoService.consultarAluno(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> alunoUseCase.consultarAluno("id-inexistente"));

        assertEquals("ID aluno não encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(alunoService).consultarAluno("id-inexistente");
    }

    @Test
    void atualizarAluno_ComDadosValidos_DeveRetornarAlunoAtualizado() {
        when(escolaService.consultarEscola(anyString())).thenReturn(Optional.of(escola));
        when(alunoService.atualizarAluno(any(Aluno.class))).thenReturn(aluno);

        Aluno resultado = alunoUseCase.atualizarAluno(aluno);

        assertNotNull(resultado);
        assertEquals(aluno.getId(), resultado.getId());
        verify(escolaService).consultarEscola(escola.getId());
        verify(alunoService).atualizarAluno(aluno);
    }

    @Test
    void atualizarAluno_SemEscola_DeveLancarBusinessException() {
        Aluno alunoSemEscola = Aluno.builder()
                .id("aluno-id-1")
                .nome("João Silva")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> alunoUseCase.atualizarAluno(alunoSemEscola));

        assertEquals("Deve ser informado o ID escola para cadastrar o Aluno", exception.getMessage());
        verify(escolaService, never()).consultarEscola(anyString());
        verify(alunoService, never()).atualizarAluno(any());
    }

    @Test
    void deletarAluno_ComIdValido_DeveExecutarDelecao() {
        doNothing().when(alunoService).deletarAluno(any(Aluno.class));

        assertDoesNotThrow(() -> alunoUseCase.deletarAluno("aluno-id-1"));

        verify(alunoService).deletarAluno(any(Aluno.class));
    }
}
