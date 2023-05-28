package me.relrypesan.transporteescolarlistapresenca.adapters.web.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.EscolaDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers.EscolaDtoMapper;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.EscolaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/escolas")
public class EscolaController {

    private final EscolaUseCase escolaUseCase;
    private final EscolaDtoMapper escolaDtoMapper;

    @PostMapping
    public ResponseEntity<EscolaDto> cadastrarEscola(@RequestBody EscolaDto escolaDto) {
        var escola = escolaDtoMapper.dtoToDomain(escolaDto);
        escola = escolaUseCase.cadastrarNovaEscola(escola);
        var response = escolaDtoMapper.domainToDto(escola);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> listarEscolas() {
        var listaEscolas = escolaUseCase.listarTodasEscola();
        var response = listaEscolas.stream()
                .map(escolaDtoMapper::domainToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id_escola}")
    public ResponseEntity<?> consultarEscola(@PathVariable("id_escola") String idEscola) {
        var escola = escolaUseCase.consultarEscola(idEscola);
        var response = escolaDtoMapper.domainToDto(escola);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id_escola}")
    public ResponseEntity<?> atualizarEscola(@PathVariable("id_escola") String idEscola, @RequestBody EscolaDto escolaDto) {
        escolaDto.setId(idEscola);
        var escola = escolaDtoMapper.dtoToDomain(escolaDto);
        escola = escolaUseCase.atualizarEscola(escola);
        return ResponseEntity.ok(escola);
    }

    @DeleteMapping("/{id_escola}")
    public ResponseEntity<?> deletarEscola(@PathVariable("id_escola") String idEscola) {
        escolaUseCase.deletarEscola(idEscola);
        return ResponseEntity.noContent().build();
    }
}
