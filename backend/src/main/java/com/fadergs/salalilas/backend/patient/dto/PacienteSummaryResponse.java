package com.fadergs.salalilas.backend.patient.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PacienteSummaryResponse(
        UUID pacienteId,
        String nome,
        String cpf,
        String ultimoStatus,
        LocalDate ultimoAtendimento
) {
}
