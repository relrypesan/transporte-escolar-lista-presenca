package me.relrypesan.transporteescolarlistapresenca.core.domain.ports.usuario;

import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Usuario;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.ConsultarPort;

import java.util.Optional;

public interface ConsultarUsuarioPort extends ConsultarPort<Usuario> {
    Optional<Usuario> consultarPorUsername(String username);
}
