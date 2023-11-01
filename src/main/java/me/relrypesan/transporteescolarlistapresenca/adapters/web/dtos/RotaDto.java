package me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RotaDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("usuario")
    private UsuarioDto usuario;
    @JsonProperty("hora_inicial")
    private String horaInicial;
    @JsonProperty("hora_final")
    private String horaFinal;
}
