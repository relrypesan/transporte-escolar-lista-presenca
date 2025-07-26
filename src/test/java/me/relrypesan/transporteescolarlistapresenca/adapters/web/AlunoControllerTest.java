package me.relrypesan.transporteescolarlistapresenca.adapters.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.AlunoDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.EscolaDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers.AlunoDtoMapper;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.SexoEnum;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.AlunoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoUseCase alunoUseCase;

    @MockBean
    private AlunoDtoMapper alunoDtoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Aluno aluno;
    private AlunoDto alunoDto;

    @BeforeEach
    void setUp() {
        Escola escola = Escola.builder()
                .id("escola-id-1")
                .nome("Escola Teste")
                .build();

        aluno = Aluno.builder()
                .id("aluno-id-1")
                .nome("João Silva")
                .sexo(SexoEnum.MASCULINO)
                .dataNascimento(LocalDate.of(2010, 5, 15))
                .escola(escola)
                .build();

        EscolaDto escolaDto = new EscolaDto("escola-id-1", "Escola Teste", null);

        alunoDto = new AlunoDto("aluno-id-1", "João Silva", SexoEnum.MASCULINO, 
                LocalDate.of(2010, 5, 15), null, escolaDto, null, null, null);
    }

    @Test
    void cadastrarAluno_ComDadosValidos_DeveRetornarStatus201() throws Exception {
        when(alunoDtoMapper.dtoToDomain(any(AlunoDto.class))).thenReturn(aluno);
        when(alunoUseCase.cadastrarNovoAluno(any(Aluno.class))).thenReturn(aluno);
        when(alunoDtoMapper.domainToDto(any(Aluno.class))).thenReturn(alunoDto);

        mockMvc.perform(post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("aluno-id-1"))
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(alunoDtoMapper).dtoToDomain(any(AlunoDto.class));
        verify(alunoUseCase).cadastrarNovoAluno(any(Aluno.class));
        verify(alunoDtoMapper).domainToDto(any(Aluno.class));
    }

    @Test
    void cadastrarAluno_ComDadosInvalidos_DeveRetornarStatus400() throws Exception {
        when(alunoDtoMapper.dtoToDomain(any(AlunoDto.class))).thenReturn(aluno);
        when(alunoUseCase.cadastrarNovoAluno(any(Aluno.class)))
                .thenThrow(new BusinessException("Deve ser informado o ID escola para cadastrar o Aluno"));

        mockMvc.perform(post("/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoDto)))
                .andExpect(status().isBadRequest());

        verify(alunoUseCase).cadastrarNovoAluno(any(Aluno.class));
    }

    @Test
    void listarAlunos_DeveRetornarStatus200ComPaginacao() throws Exception {
        Page<Aluno> pageAlunos = new PageImpl<>(Collections.singletonList(aluno));
        when(alunoUseCase.listarAlunos(any(), any())).thenReturn(pageAlunos);
        when(alunoDtoMapper.domainToDto(any(Aluno.class))).thenReturn(alunoDto);

        mockMvc.perform(get("/alunos")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value("aluno-id-1"));

        verify(alunoUseCase).listarAlunos(any(), any());
        verify(alunoDtoMapper).domainToDto(any(Aluno.class));
    }

    @Test
    void consultarAluno_ComIdExistente_DeveRetornarStatus200() throws Exception {
        when(alunoUseCase.consultarAluno(anyString())).thenReturn(aluno);
        when(alunoDtoMapper.domainToDto(any(Aluno.class))).thenReturn(alunoDto);

        mockMvc.perform(get("/alunos/{id_aluno}", "aluno-id-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("aluno-id-1"))
                .andExpect(jsonPath("$.nome").value("João Silva"));

        verify(alunoUseCase).consultarAluno("aluno-id-1");
        verify(alunoDtoMapper).domainToDto(aluno);
    }

    @Test
    void consultarAluno_ComIdInexistente_DeveRetornarStatus404() throws Exception {
        when(alunoUseCase.consultarAluno(anyString()))
                .thenThrow(new BusinessException(HttpStatus.NOT_FOUND, "ID aluno não encontrado"));

        mockMvc.perform(get("/alunos/{id_aluno}", "id-inexistente"))
                .andExpect(status().isNotFound());

        verify(alunoUseCase).consultarAluno("id-inexistente");
    }

    @Test
    void atualizarAluno_ComDadosValidos_DeveRetornarStatus200() throws Exception {
        when(alunoDtoMapper.dtoToDomain(any(AlunoDto.class))).thenReturn(aluno);
        when(alunoUseCase.atualizarAluno(any(Aluno.class))).thenReturn(aluno);
        when(alunoDtoMapper.domainToDto(any(Aluno.class))).thenReturn(alunoDto);

        mockMvc.perform(patch("/alunos/{id_aluno}", "aluno-id-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alunoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("aluno-id-1"));

        verify(alunoDtoMapper).dtoToDomain(any(AlunoDto.class));
        verify(alunoUseCase).atualizarAluno(any(Aluno.class));
        verify(alunoDtoMapper).domainToDto(any(Aluno.class));
    }

    @Test
    void deletarAluno_ComIdValido_DeveRetornarStatus204() throws Exception {
        doNothing().when(alunoUseCase).deletarAluno(anyString());

        mockMvc.perform(delete("/alunos/{id_aluno}", "aluno-id-1"))
                .andExpect(status().isNoContent());

        verify(alunoUseCase).deletarAluno("aluno-id-1");
    }
}
