package com.fadergs.salalilas.backend.medicalrecord.legal.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ObsJuridicaResponse(
        UUID id,
        UUID agendamentoId,
        String pacienteNome,
        String encaminhamentosLegais,
        String criadoPor,
        OffsetDateTime criadoEm
) {
}
