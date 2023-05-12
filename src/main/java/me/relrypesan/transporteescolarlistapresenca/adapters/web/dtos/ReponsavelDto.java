package me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.ParentescoEnum;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.SexoEnum;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReponsavelDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("nome")
    private String nome;
    @JsonProperty("sexo")
    private SexoEnum sexo;
    @JsonProperty("data_nascimento")
    private LocalDate dataNascimento;
    @JsonProperty("endereco")
    private EnderecoDto endereco;
    @JsonProperty("telefones")
    private List<TelefoneDto> telefones;
    @JsonProperty("parentesco")
    private ParentescoEnum parentesco;
    @JsonProperty("observacao")
    private String observacao;
}
