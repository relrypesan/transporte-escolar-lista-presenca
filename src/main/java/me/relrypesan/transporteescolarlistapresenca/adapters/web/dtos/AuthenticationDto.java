package me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDto {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
