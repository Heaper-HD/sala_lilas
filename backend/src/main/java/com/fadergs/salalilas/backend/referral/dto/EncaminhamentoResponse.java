package com.fadergs.salalilas.backend.referral.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EncaminhamentoResponse(
        UUID agendamentoId,
        String status,
        String origemPerfil,
        String destinoPerfil,
        LocalDateTime criadoEm
) {
}
