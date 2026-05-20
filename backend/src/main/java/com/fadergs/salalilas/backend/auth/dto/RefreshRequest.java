package com.fadergs.salalilas.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "Refresh token é orbigatório") String refreshToken
) {
}
