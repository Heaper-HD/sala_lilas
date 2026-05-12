package com.fadergs.salalilas.backend.security;

import com.fadergs.salalilas.backend.common.enums.PerfilUsuario;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.AuthException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {
    private SecurityUtils() {}

    public static UserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException(ErrorCode.AUTH_TOKEN_INVALID);
        }

        return (UserPrincipal) authentication.getPrincipal();
    }

    public static UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static PerfilUsuario getCurrentUserPerfil() {
        return getCurrentUser().getPerfil();
    }

    public static boolean hasProfile(PerfilUsuario perfil) {
        return getCurrentUserPerfil() == perfil;
    }

    public static boolean hasAnyProfile(PerfilUsuario... perfis) {
        PerfilUsuario current = getCurrentUserPerfil();
        for (PerfilUsuario perfil : perfis) {
            if (current == perfil) return true;
        }
        return false;
    }
}
