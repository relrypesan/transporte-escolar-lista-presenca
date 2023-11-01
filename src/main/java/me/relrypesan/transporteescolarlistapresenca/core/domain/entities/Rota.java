package me.relrypesan.transporteescolarlistapresenca.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rota {

    private String id;
    private Usuario usuario;
    private String nome;
    private String observacao;
    private LocalTime horaInicial;
    private LocalTime horaFinal;
    private List<Escola> escolas;
    private List<Aluno> alunos;

}
