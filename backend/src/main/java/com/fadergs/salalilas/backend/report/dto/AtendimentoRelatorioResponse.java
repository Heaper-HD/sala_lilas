package com.fadergs.salalilas.backend.report.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AtendimentoRelatorioResponse(
        UUID agendamentoId,
        String pacienteNome,
        LocalDate data,
        LocalTime horario,
        String status,
        String atendente,
        OffsetDateTime criadoEm
) {
}
