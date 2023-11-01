package me.relrypesan.transporteescolarlistapresenca.core.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.PermissoesEnum;
import me.relrypesan.transporteescolarlistapresenca.core.domain.enums.SexoEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    private String id;
    private String username;
    private String password;
    private String nome;
    private SexoEnum sexo;
    private LocalDate dataNascimento;
    private Endereco endereco;
    private List<Telefone> telefones;
    private String observacao;
    private Boolean ativado;
    private List<PermissoesEnum> permissoes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissoes.stream()
                .map(PermissoesEnum::getGrantedAuthority)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativado;
    }
}
