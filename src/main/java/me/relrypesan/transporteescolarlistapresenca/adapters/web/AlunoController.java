package me.relrypesan.transporteescolarlistapresenca.adapters.web;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.AlunoDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.mappers.AlunoDtoMapper;
import me.relrypesan.transporteescolarlistapresenca.core.usecase.AlunoUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoUseCase alunoUseCase;
    private final AlunoDtoMapper alunoDtoMapper;

    @PostMapping
    public ResponseEntity<?> cadastrarAluno(@RequestBody AlunoDto alunoDto) {
        var aluno = alunoDtoMapper.dtoToDomain(alunoDto);
        aluno = alunoUseCase.cadastrarNovoAluno(aluno);
        var response = alunoDtoMapper.domainToDto(aluno);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<?>> listarAlunos(Pageable pageable, @RequestParam Map<String, String> filters) {
        var listaAlunos = alunoUseCase.listarAlunos(pageable, filters);

        var response = listaAlunos.map(alunoDtoMapper::domainToDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id_aluno}")
    public ResponseEntity<?> consultarAluno(@PathVariable("id_aluno") String idAluno) {
        var aluno = alunoUseCase.consultarAluno(idAluno);
        var response = alunoDtoMapper.domainToDto(aluno);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id_aluno}")
    public ResponseEntity<?> atualizarAluno(@PathVariable("id_aluno") String idAluno, @RequestBody AlunoDto alunoDto) {
        alunoDto.setId(idAluno);
        var aluno = alunoDtoMapper.dtoToDomain(alunoDto);
        aluno = alunoUseCase.atualizarAluno(aluno);
        return ResponseEntity.ok(aluno);
    }

    @DeleteMapping("/{id_aluno}")
    public ResponseEntity<?> deletarAluno(@PathVariable("id_aluno") String idAluno) {
        alunoUseCase.deletarAluno(idAluno);
        return ResponseEntity.noContent().build();
    }
}
