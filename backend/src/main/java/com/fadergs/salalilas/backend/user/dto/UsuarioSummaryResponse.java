package com.fadergs.salalilas.backend.user.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UsuarioSummaryResponse(
        UUID id,
        String nome,
        String email,
        String perfil,
        boolean ativo,
        boolean lgpdAceito,
        OffsetDateTime lgpdDate
) {
}
