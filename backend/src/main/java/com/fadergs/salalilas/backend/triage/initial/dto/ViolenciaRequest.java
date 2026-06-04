package com.fadergs.salalilas.backend.triage.initial.dto;

import jakarta.validation.constraints.NotBlank;

public record ViolenciaRequest(
        String violencia,
        String violenciaOutro
) {
}
