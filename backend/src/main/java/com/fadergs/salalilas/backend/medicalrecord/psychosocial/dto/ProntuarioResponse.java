package com.fadergs.salalilas.backend.medicalrecord.psychosocial.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ProntuarioResponse(
        UUID id,
        UUID agendamentoId,
        String pacienteNome,
        String observacoesPsicossociais,
        String criadoPor,
        OffsetDateTime criadoEm
) {
}
