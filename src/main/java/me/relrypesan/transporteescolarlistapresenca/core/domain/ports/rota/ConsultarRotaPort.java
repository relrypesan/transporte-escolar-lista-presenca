package me.relrypesan.transporteescolarlistapresenca.core.domain.ports.rota;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Rota;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.ConsultarPort;

import java.util.List;

public interface ConsultarRotaPort extends ConsultarPort<Rota> {
    List<Rota> consultarRotasPorUsuario(String idUsuario);
}
