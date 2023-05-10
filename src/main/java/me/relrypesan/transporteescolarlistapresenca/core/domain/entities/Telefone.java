package me.relrypesan.transporteescolarlistapresenca.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Telefone {

    private String id;
    private String ddi;
    private String ddd;
    private String numero;
    private String observacao;

}
