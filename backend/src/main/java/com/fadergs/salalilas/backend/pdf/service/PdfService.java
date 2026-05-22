package com.fadergs.salalilas.backend.pdf.service;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import com.fadergs.salalilas.backend.medicalrecord.legal.dto.ObsJuridicaResponse;
import com.fadergs.salalilas.backend.medicalrecord.legal.repository.ObsJuridicaRepository;
import com.fadergs.salalilas.backend.medicalrecord.legal.service.ObsJuridicaService;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.dto.ProntuarioResponse;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.repository.ProntuarioRepository;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.service.ProntuarioService;
import com.fadergs.salalilas.backend.patient.dto.TimelineEventoResponse;
import com.fadergs.salalilas.backend.pdf.dto.PdfAtendimentoData;
import com.fadergs.salalilas.backend.pdf.generator.AtendimentoPdfGenerator;
import com.fadergs.salalilas.backend.timeline.repository.TimelineRepository;
import com.fadergs.salalilas.backend.triage.initial.dto.AnamneseInicialResponse;
import com.fadergs.salalilas.backend.triage.initial.repository.AnamneseInicialRepository;
import com.fadergs.salalilas.backend.triage.initial.service.AnamneseInicialService;
import com.fadergs.salalilas.backend.triage.technical.dto.AnamneseTecnicaResponse;
import com.fadergs.salalilas.backend.triage.technical.repository.AnamneseTecnicaRepository;
import com.fadergs.salalilas.backend.triage.technical.service.AnamneseTecnicaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfService {
    private final AgendamentoRepository agendamentoRepository;
    private final AnamneseInicialRepository anamneseInicialRepository;
    private final AnamneseTecnicaRepository anamneseTecnicaRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final ObsJuridicaRepository obsJuridicaRepository;
    private final TimelineRepository timelineRepository;
    private final AnamneseInicialService anamneseInicialService;
    private final AnamneseTecnicaService anamneseTecnicaService;
    private final ProntuarioService prontuarioService;
    private final ObsJuridicaService obsJuridicaService;
    private final AtendimentoPdfGenerator pdfGenerator;

    @Transactional(readOnly = true)
    public byte[] gerar(UUID agendamentoId, UUID usuarioId) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AGD_NOT_FOUND));

        AnamneseInicialResponse anamneseInicial = anamneseInicialRepository
                .findByAgendamentoId(agendamentoId)
                .map(anamneseInicialService::toResponse)
                .orElse(null);

        AnamneseTecnicaResponse anamneseTecnica = anamneseTecnicaRepository
                .findByAgendamentoId(agendamentoId)
                .map(anamneseTecnicaService::toResponse)
                .orElse(null);

        ProntuarioResponse prontuario = prontuarioRepository
                .findByAgendamentoId(agendamentoId)
                .map(prontuarioService::toResponse)
                .orElse(null);

        ObsJuridicaResponse obsJuridica = obsJuridicaRepository
                .findByAgendamentoId(agendamentoId)
                .map(obsJuridicaService::toResponse)
                .orElse(null);

        List<TimelineEventoResponse> timeline = timelineRepository
                .findByAgendamentoIdOrderByCriadoEmAsc(agendamentoId)
                .stream()
                .map(t -> new TimelineEventoResponse(
                        t.getEvento().name(),
                        t.getDescricao(),
                        t.getCriadoPor() != null ? t.getCriadoPor().getNome() : null,
                        t.getCriadoEm()
                ))
                .toList();

        String atendenteNome = agendamento.getAtendente() != null
                ? agendamento.getAtendente().getNome() : null;

        String geradoPor = usuarioId != null
                ? agendamento.getAtendente() != null
                    ? agendamento.getAtendente().getNome()
                    : "Sistema"
                : "Sistema";

        PdfAtendimentoData data = new PdfAtendimentoData(
                agendamento.getPaciente().getNome(),
                agendamento.getPaciente().getCpf(),
                agendamento.getData(),
                agendamento.getHorario(),
                atendenteNome,
                anamneseInicial,
                anamneseTecnica,
                prontuario,
                obsJuridica,
                timeline,
                geradoPor,
                LocalDateTime.now()
        );

        log.info("Generating PDF for appointment {}", agendamentoId);
        return pdfGenerator.generate(data);
    }
}
