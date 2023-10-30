package me.relrypesan.transporteescolarlistapresenca.core.domain.ports.escola;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;

public interface SalvarEscolaPort {
    Escola cadastrarNovaEscola(Escola escola);
    Escola atualizarEscola(Escola escola);
}
