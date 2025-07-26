package me.relrypesan.transporteescolarlistapresenca.adapters.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.AlunoDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.MarcacaoDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers.MarcacaoDtoMapper;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.TipoMarcacaoEnum;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.MarcacaoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MarcacoesController.class)
class MarcacoesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarcacaoUseCase marcacaoUseCase;

    @MockBean
    private MarcacaoDtoMapper marcacaoDtoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Marcacao marcacao;
    private MarcacaoDto marcacaoDto;

    @BeforeEach
    void setUp() {
        Aluno aluno = Aluno.builder()
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

        AlunoDto alunoDto = new AlunoDto("aluno-id-1", "João Silva", null, null, null, null, null, null, null);

        marcacaoDto = new MarcacaoDto("marcacao-id-1", LocalDate.now(), LocalTime.now(), 
                TipoMarcacaoEnum.ENTRADA, alunoDto, null);
    }

    @Test
    void cadastrarMarcacao_ComDadosValidos_DeveRetornarStatus201() throws Exception {
        when(marcacaoDtoMapper.dtoToDomain(any(MarcacaoDto.class))).thenReturn(marcacao);
        when(marcacaoUseCase.cadastrarNovaMarcacao(any(Marcacao.class))).thenReturn(marcacao);
        when(marcacaoDtoMapper.domainToDto(any(Marcacao.class))).thenReturn(marcacaoDto);

        mockMvc.perform(post("/alunos/{id_aluno}/marcacoes", "aluno-id-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcacaoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("marcacao-id-1"))
                .andExpect(jsonPath("$.tipo_marcacao").value("ENTRADA"));

        verify(marcacaoDtoMapper).dtoToDomain(any(MarcacaoDto.class));
        verify(marcacaoUseCase).cadastrarNovaMarcacao(any(Marcacao.class));
        verify(marcacaoDtoMapper).domainToDto(any(Marcacao.class));
    }

    @Test
    void cadastrarMarcacao_ComDadosInvalidos_DeveRetornarStatus400() throws Exception {
        when(marcacaoDtoMapper.dtoToDomain(any(MarcacaoDto.class))).thenReturn(marcacao);
        when(marcacaoUseCase.cadastrarNovaMarcacao(any(Marcacao.class)))
                .thenThrow(new BusinessException("Deve ser informado o ID aluno para cadastrar a Marcação"));

        mockMvc.perform(post("/alunos/{id_aluno}/marcacoes", "aluno-id-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcacaoDto)))
                .andExpect(status().isBadRequest());

        verify(marcacaoUseCase).cadastrarNovaMarcacao(any(Marcacao.class));
    }

    @Test
    void listarMarcacoes_DeveRetornarStatus200ComPaginacao() throws Exception {
        Page<Marcacao> pageMarcacoes = new PageImpl<>(Collections.singletonList(marcacao));
        when(marcacaoUseCase.listarMarcacoes(any(), any(), anyString())).thenReturn(pageMarcacoes);
        when(marcacaoDtoMapper.domainToDto(any(Marcacao.class))).thenReturn(marcacaoDto);

        mockMvc.perform(get("/alunos/{id_aluno}/marcacoes", "aluno-id-1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value("marcacao-id-1"));

        verify(marcacaoUseCase).listarMarcacoes(any(), any(), eq("aluno-id-1"));
        verify(marcacaoDtoMapper).domainToDto(any(Marcacao.class));
    }

    @Test
    void consultarMarcacao_ComIdExistente_DeveRetornarStatus200() throws Exception {
        when(marcacaoUseCase.consultarMarcacao(any(Marcacao.class))).thenReturn(marcacao);
        when(marcacaoDtoMapper.domainToDto(any(Marcacao.class))).thenReturn(marcacaoDto);

        mockMvc.perform(get("/alunos/{id_aluno}/marcacoes/{id_marcacao}", "aluno-id-1", "marcacao-id-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("marcacao-id-1"))
                .andExpect(jsonPath("$.tipo_marcacao").value("ENTRADA"));

        verify(marcacaoUseCase).consultarMarcacao(any(Marcacao.class));
        verify(marcacaoDtoMapper).domainToDto(marcacao);
    }

    @Test
    void consultarMarcacao_ComIdInexistente_DeveRetornarStatus404() throws Exception {
        when(marcacaoUseCase.consultarMarcacao(any(Marcacao.class)))
                .thenThrow(new BusinessException(HttpStatus.NOT_FOUND, "ID marcação não encontrado"));

        mockMvc.perform(get("/alunos/{id_aluno}/marcacoes/{id_marcacao}", "aluno-id-1", "id-inexistente"))
                .andExpect(status().isNotFound());

        verify(marcacaoUseCase).consultarMarcacao(any(Marcacao.class));
    }

    @Test
    void atualizarMarcacao_ComDadosValidos_DeveRetornarStatus200() throws Exception {
        when(marcacaoDtoMapper.dtoToDomain(any(MarcacaoDto.class))).thenReturn(marcacao);
        when(marcacaoUseCase.atualizarMarcacao(any(Marcacao.class))).thenReturn(marcacao);
        when(marcacaoDtoMapper.domainToDto(any(Marcacao.class))).thenReturn(marcacaoDto);

        mockMvc.perform(patch("/alunos/{id_aluno}/marcacoes/{id_marcacao}", "aluno-id-1", "marcacao-id-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(marcacaoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("marcacao-id-1"));

        verify(marcacaoDtoMapper).dtoToDomain(any(MarcacaoDto.class));
        verify(marcacaoUseCase).atualizarMarcacao(any(Marcacao.class));
        verify(marcacaoDtoMapper).domainToDto(any(Marcacao.class));
    }

    @Test
    void deletarMarcacao_ComIdValido_DeveRetornarStatus204() throws Exception {
        doNothing().when(marcacaoUseCase).deletarMarcacao(any(Marcacao.class));

        mockMvc.perform(delete("/alunos/{id_aluno}/marcacoes/{id_marcacao}", "aluno-id-1", "marcacao-id-1"))
                .andExpect(status().isNoContent());

        verify(marcacaoUseCase).deletarMarcacao(any(Marcacao.class));
    }
}
