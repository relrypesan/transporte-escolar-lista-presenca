package me.relrypesan.transporteescolarlistapresenca.core.domain.ports.marcacao;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Marcacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface ConsultarMarcacaoPort {
    Optional<Marcacao> consultarMarcacao(String idMarcacao);
    Page<Marcacao> paginarMarcacao(Pageable pageable, Map<String, String> filters, String idAluno);
}
