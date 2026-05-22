package com.fadergs.salalilas.backend.patient.dto;

import java.time.OffsetDateTime;

public record TimelineEventoResponse(
        String evento,
        String descricao,
        String responsavel,
        OffsetDateTime criadoEm
) {
}
