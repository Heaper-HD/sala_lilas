package com.fadergs.salalilas.backend.patient.service;

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
import com.fadergs.salalilas.backend.patient.dto.AtendimentoDetalheResponse;
import com.fadergs.salalilas.backend.patient.dto.PacienteDetalheResponse;
import com.fadergs.salalilas.backend.patient.dto.PacienteSummaryResponse;
import com.fadergs.salalilas.backend.patient.dto.TimelineEventoResponse;
import com.fadergs.salalilas.backend.patient.entity.Paciente;
import com.fadergs.salalilas.backend.patient.repository.PacienteRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PacienteService {
    private final PacienteRepository pacienteRepository;
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

    @Transactional(readOnly = true)
    public List<PacienteSummaryResponse> listar(String busca) {
        List<Paciente> pacientes = (busca == null || busca.isBlank())
            ? pacienteRepository.findAll()
            : pacienteRepository.buscarPorNomeOuCpf(busca);

        return pacientes.stream()
                .map(p -> {
                    List<Agendamento> agendamentos = agendamentoRepository
                            .findByPacienteIdOrderByDataDescHorarioDesc(p.getId());

                    String ultimoStatus = agendamentos.isEmpty()
                            ? null
                            : agendamentos.get(0).getStatus().name();

                    LocalDate ultimoAtendimento = agendamentos.isEmpty()
                            ? null
                            : agendamentos.get(0).getData();

                    return new PacienteSummaryResponse(
                            p.getId(),
                            p.getNome(),
                            p.getEmail(),
                            ultimoStatus,
                            ultimoAtendimento
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteDetalheResponse buscar(UUID pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PAC_NOT_FOUND));

        List<Agendamento> agendamentos = agendamentoRepository
                .findByPacienteIdOrderByDataDescHorarioDesc(pacienteId);

        List<AtendimentoDetalheResponse> atendimentos = agendamentos.stream()
                .map(this::toAtendimentoDetalhe)
                .toList();

        return new PacienteDetalheResponse(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getEmail(),
                atendimentos
        );
    }

    @Transactional(readOnly = true)
    public List<TimelineEventoResponse> timeline(UUID pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PAC_NOT_FOUND));

        List<Agendamento> agendamentos = agendamentoRepository
                .findByPacienteIdOrderByDataDescHorarioDesc(pacienteId);

        return agendamentos.stream()
                .flatMap(a -> timelineRepository
                        .findByAgendamentoIdOrderByCriadoEmAsc(a.getId())
                        .stream())
                .map(t -> new TimelineEventoResponse(
                        t.getEvento().name(),
                        t.getDescricao(),
                        t.getCriadoPor() != null ? t.getCriadoPor().getNome() : null,
                        t.getCriadoEm()
                ))
                .toList();
    }

    private AtendimentoDetalheResponse toAtendimentoDetalhe(Agendamento a) {
        AnamneseInicialResponse anamneseInicial = anamneseInicialRepository
                .findByAgendamentoId(a.getId())
                .map(anamneseInicialService::toResponse)
                .orElse(null);

        AnamneseTecnicaResponse anamneseTecnica = anamneseTecnicaRepository
                .findByAgendamentoId(a.getId())
                .map(anamneseTecnicaService::toResponse)
                .orElse(null);

        ProntuarioResponse prontuario = prontuarioRepository
                .findByAgendamentoId(a.getId())
                .map(prontuarioService::toResponse)
                .orElse(null);

        ObsJuridicaResponse obsJuridica = obsJuridicaRepository
                .findByAgendamentoId(a.getId())
                .map(obsJuridicaService::toResponse)
                .orElse(null);

        List<TimelineEventoResponse> timeline = timelineRepository
                .findByAgendamentoIdOrderByCriadoEmAsc(a.getId())
                .stream()
                .map(t -> new TimelineEventoResponse(
                        t.getEvento().name(),
                        t.getDescricao(),
                        t.getCriadoPor() != null ? t.getCriadoPor().getNome() : null,
                        t.getCriadoEm()
                ))
                .toList();

        return new AtendimentoDetalheResponse(
                a.getId(),
                a.getData(),
                a.getHorario(),
                a.getStatus().name(),
                anamneseInicial,
                anamneseTecnica,
                prontuario,
                obsJuridica,
                timeline
        );
    }
}
