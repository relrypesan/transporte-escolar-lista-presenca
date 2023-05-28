package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import me.relrypesan.transporteescolarlistapresenca.core.domain.service.EscolaService;
import me.relrypesan.transporteescolarlistapresenca.utils.EscolaFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EscolaUseCaseTest {

    @Mock
    private EscolaService escolaService;

    @InjectMocks
    private EscolaUseCase escolaUseCase;


    @Test
    void cadastrarNovaEscola() {
        var escola = EscolaFactory.criarEscola();

        when(escolaService.cadastrarEscola(any()))
                .thenReturn(escola);

        var escolaNova = escolaUseCase.cadastrarNovaEscola(escola);

        assertNotNull(escolaNova);
        assertNotNull(escolaNova.getId());

        verify(escolaService, only()).cadastrarEscola(any());
    }

    @Test
    void listarTodasEscola() {
        var escola = EscolaFactory.criarEscola();

        when(escolaService.listarEscolas())
                .thenReturn(List.of(escola));

        var listaEscolas = escolaUseCase.listarTodasEscola();

        assertNotNull(listaEscolas);
        assertTrue(listaEscolas.size() > 0);

        verify(escolaService, only()).listarEscolas();
    }

    @Test
    void consultarEscola() {
        var escola = EscolaFactory.criarEscola();

        when(escolaService.consultarEscola(any()))
                .thenReturn(Optional.of(escola));

        var escolaConsultada = escolaUseCase.consultarEscola("escola-1234");

        assertNotNull(escolaConsultada);

        verify(escolaService, only()).consultarEscola(any());
    }

    @Test
    void atualizarEscola() {
        var escola = EscolaFactory.criarEscola();

        when(escolaService.atualizarEscola(any()))
                .thenReturn(escola);

        var escolaAtualizada = escolaUseCase.atualizarEscola(escola);

        assertNotNull(escolaAtualizada);

        verify(escolaService, only()).atualizarEscola(any());
    }

    @Test
    void deletarEscola() {
        escolaUseCase.deletarEscola("escola-1234");

        verify(escolaService, only()).deletarEscola(any());
    }
}