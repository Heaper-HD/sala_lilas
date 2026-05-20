package com.fadergs.salalilas.backend.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String perfil,
        boolean lgpdPendente
) {
}
