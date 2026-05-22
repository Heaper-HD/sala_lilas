package com.fadergs.salalilas.backend.patient.dto;

import com.fadergs.salalilas.backend.medicalrecord.legal.dto.ObsJuridicaResponse;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.dto.ProntuarioResponse;
import com.fadergs.salalilas.backend.triage.initial.dto.AnamneseInicialResponse;
import com.fadergs.salalilas.backend.triage.technical.dto.AnamneseTecnicaResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record AtendimentoDetalheResponse(
        UUID agendamentoId,
        LocalDate data,
        LocalTime horario,
        String status,
        AnamneseInicialResponse anamneseInicial,
        AnamneseTecnicaResponse anamneseTecnica,
        ProntuarioResponse prontuario,
        ObsJuridicaResponse obsJuridica,
        List<TimelineEventoResponse> timeline
) {
}
