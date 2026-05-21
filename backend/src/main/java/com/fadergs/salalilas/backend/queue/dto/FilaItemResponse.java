package com.fadergs.salalilas.backend.queue.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record FilaItemResponse(
        UUID agendamentoId,
        String pacienteNome,
        LocalTime horario,
        String encaminhadoPor,
        LocalDateTime encaminhadoEm,
        String status
) {
}
