package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.EscolaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EscolaUseCaseTest {

    @Mock
    private EscolaService escolaService;

    @InjectMocks
    private EscolaUseCase escolaUseCase;

    private Escola escola;

    @BeforeEach
    void setUp() {
        escola = Escola.builder()
                .id("escola-id-1")
                .nome("Escola Municipal João Silva")
                .build();
    }

    @Test
    void cadastrarNovaEscola_ComDadosValidos_DeveRetornarEscolaSalva() {
        when(escolaService.cadastrarEscola(any(Escola.class))).thenReturn(escola);

        Escola resultado = escolaUseCase.cadastrarNovaEscola(escola);

        assertNotNull(resultado);
        assertEquals(escola.getId(), resultado.getId());
        assertEquals(escola.getNome(), resultado.getNome());
        verify(escolaService).cadastrarEscola(escola);
    }

    @Test
    void listarTodasEscola_DeveRetornarListaDeEscolas() {
        List<Escola> escolas = Arrays.asList(escola, 
                Escola.builder().id("escola-id-2").nome("Escola Estadual Maria").build());
        
        when(escolaService.listarEscolas()).thenReturn(escolas);

        List<Escola> resultado = escolaUseCase.listarTodasEscola();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(escola.getId(), resultado.get(0).getId());
        verify(escolaService).listarEscolas();
    }

    @Test
    void consultarEscola_ComIdValido_DeveRetornarEscola() {
        when(escolaService.consultarEscola(anyString())).thenReturn(Optional.of(escola));

        Escola resultado = escolaUseCase.consultarEscola("escola-id-1");

        assertNotNull(resultado);
        assertEquals(escola.getId(), resultado.getId());
        assertEquals(escola.getNome(), resultado.getNome());
        verify(escolaService).consultarEscola("escola-id-1");
    }

    @Test
    void consultarEscola_ComIdInexistente_DeveLancarBusinessException() {
        when(escolaService.consultarEscola(anyString())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, 
                () -> escolaUseCase.consultarEscola("id-inexistente"));

        assertEquals("ID escola não encontrado", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        verify(escolaService).consultarEscola("id-inexistente");
    }

    @Test
    void atualizarEscola_ComDadosValidos_DeveRetornarEscolaAtualizada() {
        when(escolaService.atualizarEscola(any(Escola.class))).thenReturn(escola);

        Escola resultado = escolaUseCase.atualizarEscola(escola);

        assertNotNull(resultado);
        assertEquals(escola.getId(), resultado.getId());
        verify(escolaService).atualizarEscola(escola);
    }

    @Test
    void deletarEscola_ComIdValido_DeveExecutarDelecao() {
        doNothing().when(escolaService).deletarEscola(any(Escola.class));

        assertDoesNotThrow(() -> escolaUseCase.deletarEscola("escola-id-1"));

        verify(escolaService).deletarEscola(any(Escola.class));
    }
}
