package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.PermissoesEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rotas")
public class RotaEntity {

    @Id
    private String id;
    @DBRef
    private UsuarioEntity usuario;
    private String nome;
    private String observacao;
    private LocalTime horaInicial;
    private LocalTime horaFinal;
    @DBRef
    private List<EscolaEntity> escolas;
    @DBRef
    private List<AlunoEntity> alunos;

}
