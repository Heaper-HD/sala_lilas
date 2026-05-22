package com.fadergs.salalilas.backend.medicalrecord.legal.dto;

import jakarta.validation.constraints.NotBlank;

public record ObsJuridicaRequest(
        @NotBlank String encaminhamentosLegais
) {
}
