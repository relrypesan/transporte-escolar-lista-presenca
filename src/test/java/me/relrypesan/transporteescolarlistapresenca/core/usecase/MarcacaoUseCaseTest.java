package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.TipoMarcacaoEnum;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.AlunoService;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.MarcacaoService;
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
class MarcacaoUseCaseTest {

    @Mock
    private AlunoService alunoService;

    @Mock
    private MarcacaoService marcacaoService;

    @InjectMocks
    private MarcacaoUseCase marcacaoUseCase;

    private Aluno aluno;
    private Marcacao marcacao;

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
    }

    @Test
    void cadastrarNovaMarcacao_ComAlunoValido_DeveRetornarMarcacaoSalva() {
        when(alunoService.consultarAluno(anyString())).thenReturn(Optional.of(aluno));
        when(marcacaoService.cadastrarMarcacao(any(Marcacao.class))).thenReturn(marcacao);

        Marcacao resultado = marcacaoUseCase.cadastrarNovaMarcacao(marcacao);

        assertNotNull(resultado);
        assertEquals(marcacao.getId(), resultado.getId());
        assertEquals(marcacao.getTipoMarcacao(), resultado.getTipoMarcacao());
        verify(alunoService).consultarAluno(aluno.getId());
        verify(marcacaoService).cadastrarMarcacao(marcacao);
    }

    @Test
    void cadastrarNovaMarcacao_SemAluno_DeveLancarBusinessException() {
        Marcacao marcacaoSemAluno = Marcacao.builder()
                .dataMarcacao(LocalDate.now())
                .tipoMarcacao(TipoMarcacaoEnum.ENTRADA)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> marcacaoUseCase.cadastrarNovaMarcacao(marcacaoSemAluno));

        assertEquals("Deve ser informado o ID aluno para cadastrar a Marcação", exception.getMessage());
        verify(alunoService, never()).consultarAluno(anyString());
        verify(marcacaoService, never()).cadastrarMarcacao(any());
    }

    @Test
    void cadastrarNovaMarcacao_ComAlunoInexistente_DeveLancarBusinessException() {
        when(alunoService.consultarAluno(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> marcacaoUseCase.cadastrarNovaMarcacao(marcacao));

        assertEquals("ID aluno não encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(alunoService).consultarAluno(aluno.getId());
        verify(marcacaoService, never()).cadastrarMarcacao(any());
    }

    @Test
    void listarMarcacoes_DeveRetornarPaginaDeMarcacoes() {
        Pageable pageable = PageRequest.of(0, 10);
        Map<String, String> filters = new HashMap<>();
        String idAluno = "aluno-id-1";
        Page<Marcacao> pageMarcacoes = new PageImpl<>(Collections.singletonList(marcacao));

        when(marcacaoService.paginarMarcacao(pageable, filters, idAluno)).thenReturn(pageMarcacoes);

        Page<Marcacao> resultado = marcacaoUseCase.listarMarcacoes(pageable, filters, idAluno);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(marcacao, resultado.getContent().get(0));
        verify(marcacaoService).paginarMarcacao(pageable, filters, idAluno);
    }

    @Test
    void consultarMarcacao_ComIdValido_DeveRetornarMarcacao() {
        when(marcacaoService.consultarMarcacao(anyString())).thenReturn(Optional.of(marcacao));

        Marcacao resultado = marcacaoUseCase.consultarMarcacao(marcacao);

        assertNotNull(resultado);
        assertEquals(marcacao.getId(), resultado.getId());
        assertEquals(marcacao.getTipoMarcacao(), resultado.getTipoMarcacao());
        verify(marcacaoService).consultarMarcacao(marcacao.getId());
    }

    @Test
    void consultarMarcacao_ComIdInexistente_DeveLancarBusinessException() {
        when(marcacaoService.consultarMarcacao(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> marcacaoUseCase.consultarMarcacao(marcacao));

        assertEquals("ID marcação não encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(marcacaoService).consultarMarcacao(marcacao.getId());
    }

    @Test
    void atualizarMarcacao_ComDadosValidos_DeveRetornarMarcacaoAtualizada() {
        when(marcacaoService.consultarMarcacao(anyString())).thenReturn(Optional.of(marcacao));
        when(marcacaoService.atualizarMarcacao(any(Marcacao.class))).thenReturn(marcacao);

        Marcacao resultado = marcacaoUseCase.atualizarMarcacao(marcacao);

        assertNotNull(resultado);
        assertEquals(marcacao.getId(), resultado.getId());
        verify(marcacaoService).consultarMarcacao(aluno.getId());
        verify(marcacaoService).atualizarMarcacao(marcacao);
    }

    @Test
    void atualizarMarcacao_SemAluno_DeveLancarBusinessException() {
        Marcacao marcacaoSemAluno = Marcacao.builder()
                .id("marcacao-id-1")
                .dataMarcacao(LocalDate.now())
                .tipoMarcacao(TipoMarcacaoEnum.ENTRADA)
                .build();

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> marcacaoUseCase.atualizarMarcacao(marcacaoSemAluno));

        assertEquals("Deve ser informado o ID aluno para cadastrar a marcação", exception.getMessage());
        verify(marcacaoService, never()).consultarMarcacao(anyString());
        verify(marcacaoService, never()).atualizarMarcacao(any());
    }

    @Test
    void deletarMarcacao_ComMarcacaoValida_DeveExecutarDelecao() {
        doNothing().when(marcacaoService).deletarMarcacao(any(Marcacao.class));

        assertDoesNotThrow(() -> marcacaoUseCase.deletarMarcacao(marcacao));

        verify(marcacaoService).deletarMarcacao(marcacao);
    }
}
