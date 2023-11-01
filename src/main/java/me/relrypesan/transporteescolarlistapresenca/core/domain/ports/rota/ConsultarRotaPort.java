package me.relrypesan.transporteescolarlistapresenca.core.domain.ports.rota;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Rota;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.ConsultarPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface ConsultarRotaPort extends ConsultarPort<Rota> {
}
