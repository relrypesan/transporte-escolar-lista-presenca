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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

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

        validaCaracteresETamanhoSenha(usuario);

        return salvarUsuarioPort.cadastrar(usuario);
    }

    private void validaCaracteresETamanhoSenha(Usuario usuario) {
        String senha = usuario.getPassword();
        if (senha == null) throw new BusinessException("Deve ser informado o 'password' para o cadastro do usuario");

        // Define a regex que permite apenas caracteres alfanuméricos e alguns símbolos especiais
        String regex = "^[a-zA-Z0-9!@#$%^&*()_+-=<>?]{6,}$";

        // Cria um padrão de regex a partir da string regex
        Pattern pattern = Pattern.compile(regex);

        // Compara a senha com o padrão de regex
        if (!pattern.matcher(senha).matches()) {
            throw new BusinessException("A senha deve conter apenas caracteres a-z, A-Z, 0-9 e/ou simbolos especiais");
        }

        usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
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
        validaCaracteresETamanhoSenha(usuario);
        return salvarUsuarioPort.atualizar(usuario);
    }

}
