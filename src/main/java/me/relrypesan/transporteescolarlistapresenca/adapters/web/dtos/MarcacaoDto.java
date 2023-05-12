package me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.TipoMarcacaoEnum;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcacaoDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("data_marcacao")
    private LocalDate dataMarcacao;
    @JsonProperty("hora_marcacao")
    private LocalTime horaMarcacao;
    @JsonProperty("tipo_marcacao")
    private TipoMarcacaoEnum tipoMarcacao;
    @JsonProperty("aluno")
    private AlunoDto aluno;
    @JsonProperty("observacao")
    private String observacao;
}
