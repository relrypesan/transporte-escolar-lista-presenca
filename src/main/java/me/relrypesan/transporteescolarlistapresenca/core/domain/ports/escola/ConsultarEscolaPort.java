package me.relrypesan.transporteescolarlistapresenca.core.domain.ports.escola;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Escola;

import java.util.List;
import java.util.Optional;

public interface ConsultarEscolaPort {
    Optional<Escola> consultarEscola(String idEscola);
    List<Escola> listarTodasEscola();
}
