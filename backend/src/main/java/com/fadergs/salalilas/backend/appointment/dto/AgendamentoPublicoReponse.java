package com.fadergs.salalilas.backend.appointment.dto;

import java.util.UUID;

public record AgendamentoPublicoReponse(
        UUID agendamentoId,
        String status
) {
}
