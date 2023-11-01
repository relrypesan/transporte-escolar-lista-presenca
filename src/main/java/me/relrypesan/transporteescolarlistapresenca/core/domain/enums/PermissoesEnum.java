package me.relrypesan.transporteescolarlistapresenca.core.domain.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum PermissoesEnum {
    CONSULTAR_ALUNO,
    LISTAR_ALUNO,
    SALVAR_ALUNO,
    DELETAR_ALUNO,

    CONSULTAR_ESCOLA,
    LISTAR_ESCOLA,
    SALVAR_ESCOLA,
    DELETAR_ESCOLA,

    CONSULTAR_ROTA,
    LISTAR_ROTA,
    SALVAR_ROTA,
    DELETAR_ROTA,

    CONSULTAR_USUARIO,
    LISTAR_USUARIO,
    SALVAR_USUARIO,

    ADMIN_MASTER;

    public GrantedAuthority getGrantedAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }
}
