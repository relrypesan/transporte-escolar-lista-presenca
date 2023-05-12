package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities;

import lombok.*;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.TipoMarcacaoEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "marcacoes")
public class MarcacaoEntity extends BaseEntity {

    @Id
    private String id;

    private LocalDate dataMarcacao;
    private LocalTime horaMarcacao;
    private TipoMarcacaoEnum tipoMarcacao;
    @DBRef(lazy = true)
    private AlunoEntity aluno;
    private String observacao;

}
