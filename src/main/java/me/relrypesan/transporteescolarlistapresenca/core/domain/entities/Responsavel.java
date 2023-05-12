package me.relrypesan.transporteescolarlistapresenca.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.ParentescoEnum;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Responsavel {

    private String id;
    private String nome;
    private String sexo;
    private LocalDate dataNascimento;
    private Endereco endereco;
    private List<Telefone> telefones;
    private ParentescoEnum parentesco;
    private String observacao;

}
