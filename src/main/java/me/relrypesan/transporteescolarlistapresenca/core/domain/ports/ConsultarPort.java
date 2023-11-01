package me.relrypesan.transporteescolarlistapresenca.core.domain.ports;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface ConsultarPort<T> {
    Optional<T> consultar(String id);
    Page<T> paginar(Pageable pageable, Map<String, String> filters);
}
