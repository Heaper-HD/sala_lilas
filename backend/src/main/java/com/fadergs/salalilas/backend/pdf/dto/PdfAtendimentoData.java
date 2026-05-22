package com.fadergs.salalilas.backend.pdf.dto;

import com.fadergs.salalilas.backend.medicalrecord.legal.dto.ObsJuridicaResponse;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.dto.ProntuarioResponse;
import com.fadergs.salalilas.backend.patient.dto.TimelineEventoResponse;
import com.fadergs.salalilas.backend.triage.initial.dto.AnamneseInicialResponse;
import com.fadergs.salalilas.backend.triage.technical.dto.AnamneseTecnicaResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record PdfAtendimentoData(
        String pacienteNome,
        String pacienteCpf,
        LocalDate data,
        LocalTime horario,
        String atendenteNome,

        AnamneseInicialResponse anamneseInicial,
        AnamneseTecnicaResponse anamneseTecnica,
        ProntuarioResponse prontuario,
        ObsJuridicaResponse obsJuridica,
        List<TimelineEventoResponse> timeline,

        String geradoPor,
        LocalDateTime geradoEm
) {
}
