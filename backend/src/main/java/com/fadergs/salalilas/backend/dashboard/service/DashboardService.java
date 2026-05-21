package com.fadergs.salalilas.backend.dashboard.service;

import com.fadergs.salalilas.backend.appointment.dto.AgendamentoDashboardResponse;
import com.fadergs.salalilas.backend.appointment.dto.ContadoresResponse;
import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.common.enums.TipoEventoTimeline;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import com.fadergs.salalilas.backend.timeline.service.TimelineService;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import com.fadergs.salalilas.backend.user.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TimelineService timelineService;

    @Transactional
    public List<AgendamentoDashboardResponse> listarAgendamentos(LocalDate data) {
        LocalDate targetDate = data != null ? data : LocalDate.now();

        return agendamentoRepository
                .findByDataAndStatusOrderByHorarioAsc(targetDate, StatusAtendimento.AGENDADO)
                .stream()
                .map(a -> new AgendamentoDashboardResponse(
                        a.getId(),
                        a.getPaciente().getNome(),
                        a.getHorario(),
                        a.getStatus().name()
                ))
                .toList();
    }

    public ContadoresResponse contadores() {
        LocalDate hoje = LocalDate.now();

        long aguardando = agendamentoRepository
                .countByDataAndStatus(hoje, StatusAtendimento.AGENDADO);

        long emAtendimento = agendamentoRepository
                .countByDataAndStatus(hoje, StatusAtendimento.TRIAGEM);

        return new ContadoresResponse(aguardando, emAtendimento);
    }

    @Transactional
    public AgendamentoDashboardResponse checkin(UUID agendamentoId, UUID usuarioId) {
        Agendamento agendamento = findOrThrow(agendamentoId);
        Usuario usuario = findUsuarioOrThrow(usuarioId);

        if (agendamento.getStatus() != StatusAtendimento.AGENDADO) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        agendamento.setStatus(StatusAtendimento.TRIAGEM);
        agendamento.setAtendente(usuario);
        agendamentoRepository.save(agendamento);

        timelineService.registrar(
                agendamento,
                TipoEventoTimeline.CHECKIN_REALIZADO,
                "Cehck-in realizado pelo atendente " + usuario.getNome(),
                usuario
        );

        log.info("Check-in for appointment {} by {}", agendamentoId, usuario.getEmail());

        return new AgendamentoDashboardResponse(
                agendamento.getId(),
                agendamento.getPaciente().getNome(),
                agendamento.getHorario(),
                agendamento.getStatus().name()
        );
    }

    @Transactional
    public AgendamentoDashboardResponse naoVeio(UUID agendamentoId, UUID usuarioId) {
        Agendamento agendamento = findOrThrow(agendamentoId);
        Usuario usuario = findUsuarioOrThrow(usuarioId);

        if (agendamento.getStatus() != StatusAtendimento.AGENDADO) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        agendamento.setStatus(StatusAtendimento.FINALIZADO);
        agendamentoRepository.save(agendamento);

        timelineService.registrar(
                agendamento,
                TipoEventoTimeline.NAO_VEIO,
                "Paciente Não compareceu. Atendimento finalizado por " + usuario.getNome(),
                usuario
        );

        log.info("Não veio por appoint {} by {}", agendamentoId, usuario.getEmail());

        return new AgendamentoDashboardResponse(
                agendamento.getId(),
                agendamento.getPaciente().getNome(),
                agendamento.getHorario(),
                agendamento.getStatus().name()
        );
    }

    private Agendamento findOrThrow(UUID id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AGD_NOT_FOUND));
    }

    private Usuario findUsuarioOrThrow(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USR_NOT_FOUND));
    }
}
