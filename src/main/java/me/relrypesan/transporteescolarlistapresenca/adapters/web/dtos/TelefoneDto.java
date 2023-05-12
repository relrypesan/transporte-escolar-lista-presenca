package me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelefoneDto {

    private String id;
    private String ddi;
    private String ddd;
    private String numero;
    private String ramal;
    private String observacao;

}
