package me.relrypesan.transporteescolarlistapresenca.adapters.persistence.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class UsuarioEntity {

    @Id
    private String id;

    private PessoaEntity pessoa;
    private String username;
    private String password;

}
