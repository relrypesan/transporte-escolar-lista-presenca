package me.relrypesan.transporteescolarlistapresenca.adapters.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.MarcacaoDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers.MarcacaoDtoMapper;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Aluno;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.MarcacaoUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/alunos/{id_aluno}/marcacoes")
public class MarcacoesController {

    private final MarcacaoUseCase marcacaoUseCase;
    private final MarcacaoDtoMapper marcacaoDtoMapper;

    @PostMapping
    public ResponseEntity<?> cadastrarMarcacaoPresenca(@PathVariable("id_aluno") String idAluno,
                                                       @RequestBody MarcacaoDto marcacaoDto) {
        var marcacao = marcacaoDtoMapper.dtoToDomain(marcacaoDto);
        marcacao.setAluno(Aluno.builder()
                .id(idAluno)
                .build());
        marcacao = marcacaoUseCase.cadastrarNovaMarcacao(marcacao);
        var response = marcacaoDtoMapper.domainToDto(marcacao);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<?>> listarMarcacoes(@PathVariable("id_aluno") String idAluno,
                                                   Pageable pageable,
                                                   @RequestParam Map<String, String> filters) {
        var listaAlunos = marcacaoUseCase.listarMarcacoes(pageable, filters, idAluno);

        var response = listaAlunos.map(marcacaoDtoMapper::domainToDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id_marcacao}")
    public ResponseEntity<?> consultarMarcacao(@PathVariable("id_aluno") String idAluno,
                                            @PathVariable("id_marcacao") String idMarcacao) {
        var marcacao = marcacaoUseCase.consultarMarcacao(idMarcacao);
        var response = marcacaoDtoMapper.domainToDto(marcacao);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id_marcacao}")
    public ResponseEntity<?> atualizarMarcacao(@PathVariable("id_aluno") String idAluno,
                                            @PathVariable("id_marcacao") String idMarcacao,
                                            @RequestBody MarcacaoDto marcacaoDto) {
        marcacaoDto.setId(idMarcacao);
        var marcacao = marcacaoDtoMapper.dtoToDomain(marcacaoDto);
        marcacao = marcacaoUseCase.atualizarMarcacao(marcacao);
        return ResponseEntity.ok(marcacao);
    }

    @DeleteMapping("/{id_marcacao}")
    public ResponseEntity<?> deletarMarcacao(@PathVariable("id_aluno") String idAluno,
                                             @PathVariable("id_marcacao") String idMarcacao) {
        marcacaoUseCase.deletarMarcacao(idMarcacao);
        return ResponseEntity.noContent().build();
    }
}
