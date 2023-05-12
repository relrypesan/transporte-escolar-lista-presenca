package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import lombok.RequiredArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.AlunoService;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.MarcacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MarcacaoUseCase {
    private final AlunoService alunoService;
    private final MarcacaoService marcacaoService;

    public Marcacao cadastrarNovaMarcacao(Marcacao marcacao) {
        if (marcacao.getAluno() == null || marcacao.getAluno().getId() == null) throw new BusinessException("Deve ser informado o ID aluno para cadastrar a Marcação");

        var alunoOptional = alunoService.consultarAluno(marcacao.getAluno().getId());
        if (alunoOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID aluno não encontrado");

        marcacao = marcacaoService.cadastrarMarcacao(marcacao);
        return marcacao;
    }

    public Page<Marcacao> listarMarcacoes(Pageable pageable, Map<String, String> filters, String idAluno) {
        var pageMarcacoes = marcacaoService.paginarMarcacao(pageable, filters, idAluno);
        return pageMarcacoes;
    }

    public Marcacao consultarMarcacao(String idMarcacao) {
        var marcacaoOptional = marcacaoService.consultarMarcacao(idMarcacao);
        if (marcacaoOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID marcação não encontrado");
        return marcacaoOptional.get();
    }

    public Marcacao atualizarMarcacao(Marcacao marcacao) {
        if (marcacao.getAluno() == null || marcacao.getAluno().getId() == null) throw new BusinessException("Deve ser informado o ID aluno para cadastrar a marcação");

        var escolaOptional = marcacaoService.consultarMarcacao(marcacao.getAluno().getId());
        if (escolaOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID escola não encontrado");

        var marcacaoAtualizada = marcacaoService.atualizarMarcacao(marcacao);
        return marcacaoAtualizada;
    }

    public void deletarMarcacao(String idMarcacao) {
        marcacaoService.deletarMarcacao(Marcacao.builder()
                .id(idMarcacao)
                .build());
    }

}
