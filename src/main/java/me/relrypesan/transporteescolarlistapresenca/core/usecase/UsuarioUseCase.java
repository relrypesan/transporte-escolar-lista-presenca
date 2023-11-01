package me.relrypesan.transporteescolarlistapresenca.core.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Usuario;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.usuario.ConsultarUsuarioPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.usuario.SalvarUsuarioPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioUseCase {
    private final ConsultarUsuarioPort consultarUsuarioPort;
    private final SalvarUsuarioPort salvarUsuarioPort;

    public Usuario cadastrarNovoUsuario(Usuario usuario) {
        if (usuario.getUsername() == null) throw new BusinessException("Deve ser informado o 'username' para o cadastro do usuario");

        Optional<Usuario> usuarioOptional = consultarUsuarioPort.consultarPorUsername(usuario.getUsername());
        if (usuarioOptional.isPresent()) {
            throw new BusinessException("Já existe um usuario com este username cadastrado");
        }

        return salvarUsuarioPort.cadastrar(usuario);
    }

    public Page<Usuario> listarUsuarios(Pageable pageable, Map<String, String> filters) {
        return consultarUsuarioPort.paginar(pageable, filters);
    }

    public Usuario consultarUsuario(String idUsuario) {
        var usuarioOptional = consultarUsuarioPort.consultar(idUsuario);
        if (usuarioOptional.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID usuario não encontrado");
        return usuarioOptional.get();
    }

    public Usuario atualizarUsuario(Usuario usuario) {
        return salvarUsuarioPort.atualizar(usuario);
    }

}
