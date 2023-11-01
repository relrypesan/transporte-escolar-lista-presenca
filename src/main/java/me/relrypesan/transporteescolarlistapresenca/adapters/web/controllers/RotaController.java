package me.relrypesan.transporteescolarlistapresenca.adapters.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.RotaDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.RotaDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers.RotaDtoMapper;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.RotaUseCase;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rotas")
public class RotaController {

    private final RotaDtoMapper rotaDtoMapper;
    private final RotaUseCase rotaUseCase;

    @PostMapping
    public ResponseEntity<RotaDto> cadastrarRota(@RequestBody RotaDto rotaDto) {
        var rota = rotaDtoMapper.dtoToDomain(rotaDto);
        rota = rotaUseCase.cadastrarNovaRota(rota);
        var response = rotaDtoMapper.domainToDto(rota);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> listarRotas(Pageable pageable, @RequestParam Map<String, String> filters) {
        var listaRotas = rotaUseCase.listarRotas(pageable, filters);
        var response = listaRotas.stream()
                .map(rotaDtoMapper::domainToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id_rota}")
    public ResponseEntity<?> consultarRota(@PathVariable("id_rota") String idRota) {
        var rota = rotaUseCase.consultarRota(idRota);
        var response = rotaDtoMapper.domainToDto(rota);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id_rota}")
    public ResponseEntity<?> atualizarRota(@PathVariable("id_rota") String idRota, @RequestBody RotaDto rotaDto) {
        rotaDto.setId(idRota);
        var rota = rotaDtoMapper.dtoToDomain(rotaDto);
        rota = rotaUseCase.atualizarRota(rota);
        return ResponseEntity.ok(rota);
    }

    @DeleteMapping("/{id_rota}")
    public ResponseEntity<?> deletarRota(@PathVariable("id_rota") String idRota) {
        rotaUseCase.deletarRota(idRota);
        return ResponseEntity.noContent().build();
    }
}
