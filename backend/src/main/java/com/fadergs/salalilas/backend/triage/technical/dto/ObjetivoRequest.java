package com.fadergs.salalilas.backend.triage.technical.dto;

import jakarta.validation.constraints.NotBlank;

public record ObjetivoRequest(
        @NotBlank String objetivo,
        String objetivoOutro
) {
}
