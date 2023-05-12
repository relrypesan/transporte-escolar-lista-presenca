package me.relrypesan.transporteescolarlistapresenca.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.TipoMarcacaoEnum;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Marcacao {

    private String id;

    private LocalDate dataMarcacao;
    private LocalTime horaMarcacao;
    private TipoMarcacaoEnum tipoMarcacao;
    private Aluno aluno;
    private String observacao;

}
