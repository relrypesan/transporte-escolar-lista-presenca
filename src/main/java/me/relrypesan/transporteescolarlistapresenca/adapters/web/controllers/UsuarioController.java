package me.relrypesan.transporteescolarlistapresenca.adapters.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.UsuarioDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers.RotaDtoMapper;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers.UsuarioDtoMapper;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.RotaUseCase;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.UsuarioUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioDtoMapper usuarioDtoMapper;
    private final UsuarioUseCase usuarioUseCase;
    private final RotaDtoMapper rotaDtoMapper;
    private final RotaUseCase rotaUseCase;

    @PostMapping
    public ResponseEntity<UsuarioDto> cadastrarUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {
        var usuario = usuarioDtoMapper.dtoToDomain(usuarioDto);
        usuario = usuarioUseCase.cadastrarNovoUsuario(usuario);
        var response = usuarioDtoMapper.domainToDto(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<?>> listarUsuarios(Pageable pageable, @RequestParam Map<String, String> filters) {
        var listaUsuarios = usuarioUseCase.listarUsuarios(pageable, filters);

        var response = listaUsuarios.map(usuarioDtoMapper::domainToDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<?> consultarUsuario(@PathVariable("id_usuario") String idUsuario) {
        var usuario = usuarioUseCase.consultarUsuario(idUsuario);
        var response = usuarioDtoMapper.domainToDto(usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id_usuario}/rotas")
    public ResponseEntity<?> consultarRotas(@PathVariable("id_usuario") String idUsuario) {
        var rotas = rotaUseCase.listarRotasPorUsuario(idUsuario);
        var response = rotas.stream().map(rotaDtoMapper::domainToDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id_usuario}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable("id_usuario") String idUsuario, @RequestBody @Valid UsuarioDto usuarioDto) {
        usuarioDto.setId(idUsuario);
        var usuario = usuarioDtoMapper.dtoToDomain(usuarioDto);
        usuario = usuarioUseCase.atualizarUsuario(usuario);
        return ResponseEntity.ok(usuario);
    }

}
