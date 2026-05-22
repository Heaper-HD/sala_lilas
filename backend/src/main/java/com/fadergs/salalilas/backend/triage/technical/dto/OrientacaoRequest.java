package com.fadergs.salalilas.backend.triage.technical.dto;

import jakarta.validation.constraints.NotBlank;

public record OrientacaoRequest(
        @NotBlank String orientacao,
        String orientacaoOutro
) {
}
