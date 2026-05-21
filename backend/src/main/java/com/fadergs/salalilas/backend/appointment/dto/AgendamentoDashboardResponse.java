package com.fadergs.salalilas.backend.appointment.dto;

import java.time.LocalTime;
import java.util.UUID;

public record AgendamentoDashboardResponse(
        UUID agendamentoId,
        String pacienteNome,
        LocalTime horario,
        String status
) {
}
