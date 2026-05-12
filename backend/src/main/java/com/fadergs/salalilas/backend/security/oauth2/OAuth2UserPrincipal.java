package com.fadergs.salalilas.backend.security.oauth2;

import com.fadergs.salalilas.backend.user.entity.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class OAuth2UserPrincipal implements OAuth2User {
    private final OAuth2User delegate;
    private final Usuario usuario;

    public OAuth2UserPrincipal(OAuth2User delegate, Usuario usuario) {
        this.delegate = delegate;
        this.usuario = usuario;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name())
        );
    }

    @Override
    public String getName() {
        return usuario.getEmail();
    }
}
