package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import lombok.RequiredArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.service.EscolaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EscolaUseCase {
    private final EscolaService escolaService;

    public Escola cadastrarNovaEscola(Escola escola) {
        return escolaService.cadastrarEscola(escola);
    }

    public List<Escola> listarTodasEscola() {
        return escolaService.listarEscolas();
    }

    public Escola consultarEscola(String idEscola) {
        var escolaOptional = escolaService.consultarEscola(idEscola);
        if (escolaOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID escola não encontrado");
        return escolaOptional.get();
    }

    public Escola atualizarEscola(Escola escola) {
        var escolaAtualizada = escolaService.atualizarEscola(escola);
        return escolaAtualizada;
    }

    public void deletarEscola(String idEscola) {
        escolaService.deletarEscola(Escola.builder()
                .id(idEscola)
                .build());
    }

}
