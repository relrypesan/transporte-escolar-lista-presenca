package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import lombok.RequiredArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.aluno.ConsultarAlunoPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.marcacao.ConsultarMarcacaoPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.marcacao.DeletarMarcacaoPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.marcacao.SalvarMarcacaoPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MarcacaoUseCase {
    private final ConsultarAlunoPort consultarAlunoPort;
    private final ConsultarMarcacaoPort consultarMarcacaoPort;
    private final SalvarMarcacaoPort salvarMarcacaoPort;
    private final DeletarMarcacaoPort deletarMarcacaoPort;

    public Marcacao cadastrarNovaMarcacao(Marcacao marcacao) {
        if (marcacao.getAluno() == null || marcacao.getAluno().getId() == null) throw new BusinessException("Deve ser informado o ID aluno para cadastrar a Marcação");

        var alunoOptional = consultarAlunoPort.consultarAluno(marcacao.getAluno().getId());
        if (alunoOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID aluno não encontrado");

        marcacao = salvarMarcacaoPort.cadastrarMarcacao(marcacao);
        return marcacao;
    }

    public Page<Marcacao> listarMarcacoes(Pageable pageable, Map<String, String> filters, String idAluno) {
        return consultarMarcacaoPort.paginarMarcacao(pageable, filters, idAluno);
    }

    public Marcacao consultarMarcacao(Marcacao marcacao) {
        var marcacaoOptional = consultarMarcacaoPort.consultarMarcacao(marcacao.getId());
        if (marcacaoOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID marcação não encontrado");
        return marcacaoOptional.get();
    }

    public Marcacao atualizarMarcacao(Marcacao marcacao) {
        if (marcacao.getAluno() == null || marcacao.getAluno().getId() == null) throw new BusinessException("Deve ser informado o ID aluno para cadastrar a marcação");

        var escolaOptional = consultarMarcacaoPort.consultarMarcacao(marcacao.getAluno().getId());
        if (escolaOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID escola não encontrado");

        return salvarMarcacaoPort.atualizarMarcacao(marcacao);
    }

    public void deletarMarcacao(Marcacao marcacao) {
        deletarMarcacaoPort.deletarMarcacao(marcacao.getId());
    }

}
