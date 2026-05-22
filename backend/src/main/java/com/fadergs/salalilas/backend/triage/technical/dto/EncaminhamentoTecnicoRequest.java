package com.fadergs.salalilas.backend.triage.technical.dto;

import jakarta.validation.constraints.NotBlank;

public record EncaminhamentoTecnicoRequest(
        @NotBlank String encaminhamento,
        String encaminhamentoOutro
) {
}
