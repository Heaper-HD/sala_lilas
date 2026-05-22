package com.fadergs.salalilas.backend.triage.initial.dto;

import jakarta.validation.constraints.NotBlank;

public record ViolenciaRequest(
        @NotBlank String violencia,
        String violenciaOutro
) {
}
