package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities.UsuarioEntity;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.mappers.UsuarioEntityMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.repositories.UsuarioRepositoryImpl;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Usuario;
import me.relrypesan.transporteescolarlistapresenca.core.domain.exceptions.BusinessException;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.usuario.ConsultarUsuarioPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.usuario.DeletarUsuarioPort;
import me.relrypesan.transporteescolarlistapresenca.core.domain.ports.usuario.SalvarUsuarioPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService implements ConsultarUsuarioPort, SalvarUsuarioPort, DeletarUsuarioPort {

    private final UsuarioRepositoryImpl usuarioRepository;
    private final UsuarioEntityMapper usuarioEntityMapper;

    @Override
    public Usuario cadastrar(Usuario usuario) {
        var entity = usuarioEntityMapper.domainToEntity(usuario);
        entity = usuarioRepository.save(entity);
        return usuarioEntityMapper.entityToDomain(entity);
    }

    @Override
    public Page<Usuario> paginar(Pageable pageable, Map<String, String> filters) {
        Page<UsuarioEntity> pageEntities = usuarioRepository.findAll(pageable);
        return pageEntities.map(usuarioEntityMapper::entityToDomain);
    }

    @Override
    public Optional<Usuario> consultar(String idUsuario) {
        var entities = usuarioRepository.findById(idUsuario);
        return entities.map(usuarioEntityMapper::entityToDomain);
    }

    @Override
    public Optional<Usuario> consultarPorUsername(String username) {
        var entities = usuarioRepository.findByUsername(username);
        return entities.map(usuarioEntityMapper::entityToDomain);
    }
    @Override
    public Usuario atualizar(Usuario usuario) {
        if (usuario.getId() == null) throw new BusinessException("ID usuario deve ser informado.");

        var usuarioConsultado = consultar(usuario.getId());
        if (usuarioConsultado.isEmpty()) throw new BusinessException(HttpStatus.NOT_FOUND, "ID da usuario informado não foi encontrado: " + usuario.getId());

        return cadastrar(usuario);
    }

}
