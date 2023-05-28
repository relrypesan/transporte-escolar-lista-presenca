package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.AlunoService;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.EscolaService;
import me.relrypesan.transporteescolarlistapresenca.utils.AlunoFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoUseCaseTest {

    @Mock
    private AlunoService alunoService;

    @Mock
    private EscolaService escolaService;

    @InjectMocks
    private AlunoUseCase alunoUseCase;


    @Test
    @DisplayName("Cadastrar novo aluno com sucesso")
    void cadastrarNovoAluno() {
        var aluno = AlunoFactory.criarAluno();

        when(escolaService.consultarEscola(any()))
                .thenReturn(Optional.of(aluno.getEscola()));
        when(alunoService.cadastrarAluno(any()))
                .thenReturn(aluno);

        var retornoAluno = alunoUseCase.cadastrarNovoAluno(aluno);

        assertNotNull(retornoAluno);

        verify(escolaService, only()).consultarEscola(any());
        verify(alunoService, only()).cadastrarAluno(any());
    }

    @Test
    @DisplayName("Listar alunos paginados da base")
    void listarAlunos() {
        var pageable = Pageable.unpaged();
        var filters = Map.of("page", "1");
        List<Aluno> listaAlunos = List.of(AlunoFactory.criarAluno());
        Page<Aluno> pageMock = new PageImpl<>(listaAlunos);

        when(alunoService.paginarAlunos(any(), any()))
                .thenReturn(pageMock);

        var paginaAlunos = alunoUseCase.listarAlunos(pageable, filters);

        assertNotNull(paginaAlunos);
        assertTrue(paginaAlunos.getSize() > 0);

        verify(alunoService, only()).paginarAlunos(any(), any());
    }

    @Test
    void consultarAluno() {
        var aluno = AlunoFactory.criarAluno();

        when(alunoService.consultarAluno(any()))
                .thenReturn(Optional.of(aluno));

        var alunoConsultado = alunoUseCase.consultarAluno("aluno-1234");

        assertNotNull(alunoConsultado);

        verify(alunoService, only()).consultarAluno(any());
    }

    @Test
    void atualizarAluno() {
        var aluno = AlunoFactory.criarAluno();

        when(escolaService.consultarEscola(any()))
                .thenReturn(Optional.of(aluno.getEscola()));
        when(alunoService.atualizarAluno(any()))
                .thenReturn(aluno);

        var alunoConsultado = alunoUseCase.atualizarAluno(aluno);

        assertNotNull(alunoConsultado);

        verify(escolaService, only()).consultarEscola(any());
        verify(alunoService, only()).atualizarAluno(any());
    }

    @Test
    void deletarAluno() {

        alunoUseCase.deletarAluno("aluno-1234");

        verify(alunoService, only()).deletarAluno(any());
    }
}