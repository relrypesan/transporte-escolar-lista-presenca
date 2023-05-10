package me.relrypesan.transporteescolarlistapresenca.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.SexoEnum;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

    private String id;
    private String nome;
    private SexoEnum sexo;
    private LocalDate dataNascimento;
    private Endereco endereco;
    private Escola escola;
    private List<Pessoa> responsaveis;

}
