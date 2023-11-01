package me.relrypesan.transporteescolarlistapresenca.adapters.web.controllers;

import lombok.RequiredArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.AuthenticationDto;
import me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos.TokenDto;
import me.relrypesan.transporteescolarlistapresenca.core.domain.entities.Usuario;
import me.relrypesan.transporteescolarlistapresenca.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Value("${security.token.expires-at}")
    private Long expiresAt;

    @PostMapping("/login")
    public ResponseEntity<?> auth(@RequestBody AuthenticationDto authenticationDto) {
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authenticationDto.getUsername(), authenticationDto.getPassword());
            var auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            String token = tokenService.generateToken((Usuario) auth.getPrincipal());
            return ResponseEntity.ok(TokenDto.builder()
                            .type("Bearer")
                            .token(token)
                            .expireAt(expiresAt)
                    .build());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
