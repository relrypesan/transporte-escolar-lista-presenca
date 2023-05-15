package me.relrypesan.transporteescolarlistapresenca.adapters.web.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expire_at")
    private Long expireAt;
    @JsonProperty("type")
    private String type;
}
