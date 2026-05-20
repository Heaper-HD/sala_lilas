package com.fadergs.salalilas.backend.auth.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MeResponse(
        UUID id,
        String nome,
        String email,
        String perfil,
        boolean lgpdAceito,
        OffsetDateTime lgpdData
) {
}
