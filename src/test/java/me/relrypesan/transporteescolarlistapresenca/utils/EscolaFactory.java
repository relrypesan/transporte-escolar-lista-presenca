package me.relrypesan.transporteescolarlistapresenca.utils;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Endereco;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;

public class EscolaFactory {

    public static Escola criarEscola() {
        return Escola.builder()
                .id("escola-1234")
                .nome("Escola Municipal 1")
                .endereco(Endereco.builder()
                        .id("endereco-escola-1234")
                        .build())
                .build();
    }

}
