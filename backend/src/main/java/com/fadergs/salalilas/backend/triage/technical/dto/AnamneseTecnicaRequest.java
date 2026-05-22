package com.fadergs.salalilas.backend.triage.technical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AnamneseTecnicaRequest(
        @NotNull Boolean riscoIminente,
        @NotNull Boolean agressorConvive,
        @NotNull Boolean historicoViolencia,
        @NotNull Boolean redeApoio,
        @NotNull Boolean filhosDependentes,
        String observacoes,
        String registroAtendimento,
        String detalhamentoEncaminhamentos,
        @NotBlank String planoAcompanhamento,
        LocalDate dataRetorno,
        String planoObservacoes,
        String sinteseCaso,
        List<OrientacaoRequest> orientacoes,
        List<EncaminhamentoTecnicoRequest> encaminhamentos,
        List<ObjetivoRequest> objetivos
) {
}
