package me.relrypesan.transporteescolarlistapresenca.adapters.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.EscolaDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers.EscolaDtoMapper;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.EscolaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EscolaController.class)
class EscolaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EscolaUseCase escolaUseCase;

    @MockBean
    private EscolaDtoMapper escolaDtoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Escola escola;
    private EscolaDto escolaDto;

    @BeforeEach
    void setUp() {
        escola = Escola.builder()
                .id("escola-id-1")
                .nome("Escola Municipal João Silva")
                .build();

        escolaDto = new EscolaDto("escola-id-1", "Escola Municipal João Silva", null);
    }

    @Test
    void cadastrarEscola_ComDadosValidos_DeveRetornarStatus201() throws Exception {
        when(escolaDtoMapper.dtoToDomain(any(EscolaDto.class))).thenReturn(escola);
        when(escolaUseCase.cadastrarNovaEscola(any(Escola.class))).thenReturn(escola);
        when(escolaDtoMapper.domainToDto(any(Escola.class))).thenReturn(escolaDto);

        mockMvc.perform(post("/escolas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(escolaDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("escola-id-1"))
                .andExpect(jsonPath("$.nome").value("Escola Municipal João Silva"));

        verify(escolaDtoMapper).dtoToDomain(any(EscolaDto.class));
        verify(escolaUseCase).cadastrarNovaEscola(any(Escola.class));
        verify(escolaDtoMapper).domainToDto(any(Escola.class));
    }

    @Test
    void cadastrarEscola_ComDadosInvalidos_DeveRetornarStatus400() throws Exception {
        when(escolaDtoMapper.dtoToDomain(any(EscolaDto.class))).thenReturn(escola);
        when(escolaUseCase.cadastrarNovaEscola(any(Escola.class)))
                .thenThrow(new BusinessException("Dados inválidos"));

        mockMvc.perform(post("/escolas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(escolaDto)))
                .andExpect(status().isBadRequest());

        verify(escolaUseCase).cadastrarNovaEscola(any(Escola.class));
    }

    @Test
    void listarEscolas_DeveRetornarStatus200() throws Exception {
        List<Escola> escolas = Arrays.asList(escola);
        when(escolaUseCase.listarTodasEscola()).thenReturn(escolas);
        when(escolaDtoMapper.domainToDto(any(Escola.class))).thenReturn(escolaDto);

        mockMvc.perform(get("/escolas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("escola-id-1"));

        verify(escolaUseCase).listarTodasEscola();
        verify(escolaDtoMapper).domainToDto(any(Escola.class));
    }

    @Test
    void consultarEscola_ComIdExistente_DeveRetornarStatus200() throws Exception {
        when(escolaUseCase.consultarEscola(anyString())).thenReturn(escola);
        when(escolaDtoMapper.domainToDto(any(Escola.class))).thenReturn(escolaDto);

        mockMvc.perform(get("/escolas/{id_escola}", "escola-id-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("escola-id-1"))
                .andExpect(jsonPath("$.nome").value("Escola Municipal João Silva"));

        verify(escolaUseCase).consultarEscola("escola-id-1");
        verify(escolaDtoMapper).domainToDto(escola);
    }

    @Test
    void consultarEscola_ComIdInexistente_DeveRetornarStatus404() throws Exception {
        when(escolaUseCase.consultarEscola(anyString()))
                .thenThrow(new BusinessException(HttpStatus.NOT_FOUND, "ID escola não encontrado"));

        mockMvc.perform(get("/escolas/{id_escola}", "id-inexistente"))
                .andExpect(status().isNotFound());

        verify(escolaUseCase).consultarEscola("id-inexistente");
    }

    @Test
    void atualizarEscola_ComDadosValidos_DeveRetornarStatus200() throws Exception {
        when(escolaDtoMapper.dtoToDomain(any(EscolaDto.class))).thenReturn(escola);
        when(escolaUseCase.atualizarEscola(any(Escola.class))).thenReturn(escola);

        mockMvc.perform(patch("/escolas/{id_escola}", "escola-id-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(escolaDto)))
                .andExpect(status().isOk());

        verify(escolaDtoMapper).dtoToDomain(any(EscolaDto.class));
        verify(escolaUseCase).atualizarEscola(any(Escola.class));
    }

    @Test
    void deletarEscola_ComIdValido_DeveRetornarStatus204() throws Exception {
        doNothing().when(escolaUseCase).deletarEscola(anyString());

        mockMvc.perform(delete("/escolas/{id_escola}", "escola-id-1"))
                .andExpect(status().isNoContent());

        verify(escolaUseCase).deletarEscola("escola-id-1");
    }
}
