package me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String type;
    private String token;
    private Long expireAt;
}
