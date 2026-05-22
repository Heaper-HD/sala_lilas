package com.fadergs.salalilas.backend.medicalrecord.psychosocial.dto;

import jakarta.validation.constraints.NotBlank;

public record ProntuarioRequest(
        @NotBlank String observacoesPsicossocias
) {
}
