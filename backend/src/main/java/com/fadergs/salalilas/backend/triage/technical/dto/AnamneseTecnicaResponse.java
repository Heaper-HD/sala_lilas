package com.fadergs.salalilas.backend.triage.technical.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record AnamneseTecnicaResponse(
        UUID id,
        UUID agendamentoId,
        String pacienteNome,
        Boolean riscoIminente,
        Boolean agressorConvive,
        Boolean historicoViolencia,
        Boolean redeApoio,
        Boolean filhosDependentes,
        String observacoes,
        String registroAtendimento,
        String detalhamentoEncaminhamentos,
        String planoAcompanhamento,
        LocalDate dataRetorno,
        String planoObservacoes,
        String sinteseCaso,
        List<OrientacaoResponse> orientacoes,
        List<EncaminhamentoTecnicoResponse> encaminhamentos,
        List<ObjetivoResponse> objetivos,
        OffsetDateTime criadoEm
) {
}
