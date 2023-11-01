package me.relrypesan.transporteescolarlistapresenca.core.domain.ports.marcacao;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;

public interface SalvarMarcacaoPort {
    Marcacao cadastrarMarcacao(Marcacao marcacao);
    Marcacao atualizarMarcacao(Marcacao marcacao);
}
