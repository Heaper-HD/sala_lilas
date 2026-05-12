package com.fadergs.salalilas.backend.security;

import com.fadergs.salalilas.backend.common.enums.PerfilUsuario;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserPrincipal implements UserDetails {
    private UUID id;
    private String email;
    private String senhaHash;
    private PerfilUsuario perfil;
    private boolean ativo;

    public static UserPrincipal from(Usuario usuario) {
        return UserPrincipal.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .senhaHash(usuario.getSenhaHash())
                .perfil(usuario.getPerfil())
                .ativo(usuario.isAtivo())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return ativo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
