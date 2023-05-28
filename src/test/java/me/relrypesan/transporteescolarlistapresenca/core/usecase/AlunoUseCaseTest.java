package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import me.relrypesan.transporteescolarlistapresenca.core.domain.service.AlunoService;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.EscolaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

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

    }

    @Test
    @DisplayName("Listar alunos paginados da base")
    void listarAlunos() {
        var pageable = Pageable.unpaged();
        var filters = Map.of("page", "1");

        var listaAlunos = alunoUseCase.listarAlunos(pageable, filters);

        assertNotNull(listaAlunos);

        verify(alunoService, only()).paginarAlunos(any(), any());
    }

    @Test
    void consultarAluno() {

    }

    @Test
    void atualizarAluno() {

    }

    @Test
    void deletarAluno() {

    }
}