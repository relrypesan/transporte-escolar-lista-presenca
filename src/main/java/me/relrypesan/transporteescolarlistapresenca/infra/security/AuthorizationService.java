package me.relrypesan.transporteescolarlistapresenca.infra.security;

import lombok.RequiredArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Usuario;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.usuario.ConsultarUsuarioPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {
    private final ConsultarUsuarioPort consultarUsuarioPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = consultarUsuarioPort.consultarPorUsername(username);
        if (usuarioOptional.isEmpty()) throw new UsernameNotFoundException("Não foi encontrado o usuario: " + username);
        return usuarioOptional.get();
    }
}
