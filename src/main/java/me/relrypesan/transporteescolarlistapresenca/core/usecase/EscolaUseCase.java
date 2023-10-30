package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import lombok.RequiredArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.escola.ConsultarEscolaPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.escola.DeletarEscolaPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.escola.SalvarEscolaPort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EscolaUseCase {
    private final ConsultarEscolaPort consultarEscola;
    private final SalvarEscolaPort salvarEscolaPort;
    private final DeletarEscolaPort deletarEscolaPort;

    public Escola cadastrarNovaEscola(Escola escola) {
        return salvarEscolaPort.cadastrarNovaEscola(escola);
    }

    public List<Escola> listarTodasEscola() {
        return consultarEscola.listarTodasEscola();
    }

    public Escola consultarEscola(String idEscola) {
        var escolaOptional = consultarEscola.consultarEscola(idEscola);
        if (escolaOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID escola não encontrado");
        return escolaOptional.get();
    }

    public Escola atualizarEscola(Escola escola) {
        return salvarEscolaPort.atualizarEscola(escola);
    }

    public void deletarEscola(String idEscola) {
        deletarEscolaPort.deletarEscola(idEscola);
    }

}
