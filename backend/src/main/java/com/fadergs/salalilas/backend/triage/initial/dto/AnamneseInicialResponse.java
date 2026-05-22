package com.fadergs.salalilas.backend.triage.initial.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record AnamneseInicialResponse(
        UUID id,
        UUID agendamentoId,
        String pacienteNome,
        LocalDate dataAtendimento,
        LocalTime horario,
        String tipoAtendimento,
        Boolean primeiroAtendimento,
        String territorio,
        String corRaca,
        String sexoGenero,
        String sexoGeneroOutro,
        List<ViolenciaResponse> violencias,
        OffsetDateTime criadoEm
) {
}
