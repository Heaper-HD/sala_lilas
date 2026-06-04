package com.fadergs.salalilas.backend.triage.initial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AnamneseInicialRequest(
        @NotBlank String tipoAtendimento,
        @NotNull Boolean primeiroAtendimento,
        String territorio,
        @NotBlank String corRaca,
        @NotBlank String sexoGenero,
        String sexoGeneroOutro,
        List<ViolenciaRequest> violencias
) {
}
