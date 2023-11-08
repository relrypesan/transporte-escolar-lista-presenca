package me.relrypesan.transporteescolarlistapresenca.infra.security;

import lombok.RequiredArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.PermissoesEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(amrmr -> amrmr
                        .mvcMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .mvcMatchers("/**").hasAuthority(PermissoesEnum.ADMIN_MASTER.name())
                        .mvcMatchers(HttpMethod.POST, "/usuarios").hasAuthority(PermissoesEnum.SALVAR_USUARIO.name())
                        .mvcMatchers(HttpMethod.PATCH, "/usuarios/**").hasAuthority(PermissoesEnum.SALVAR_USUARIO.name())
                        .mvcMatchers(HttpMethod.POST, "/escolas").hasAuthority(PermissoesEnum.SALVAR_ESCOLA.name())
                        .mvcMatchers(HttpMethod.PATCH, "/escolas/**").hasAuthority(PermissoesEnum.SALVAR_ESCOLA.name())
                        .mvcMatchers(HttpMethod.POST, "/rotas").hasAuthority(PermissoesEnum.SALVAR_ROTA.name())
                        .mvcMatchers(HttpMethod.PATCH, "/rotas/**").hasAuthority(PermissoesEnum.SALVAR_ROTA.name())
                        .mvcMatchers(HttpMethod.POST, "/alunos").hasAuthority(PermissoesEnum.SALVAR_ALUNO.name())
                        .mvcMatchers(HttpMethod.PATCH, "/alunos/**").hasAuthority(PermissoesEnum.SALVAR_ALUNO.name())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
