package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.SexoEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "alunos")
public class AlunoEntity {

    @Id
    private String id;

    private String nome;
    private SexoEnum sexo;
    private LocalDate dataNascimento;
    private EnderecoEntity endereco;
    @DBRef
    private EscolaEntity escola;

}
