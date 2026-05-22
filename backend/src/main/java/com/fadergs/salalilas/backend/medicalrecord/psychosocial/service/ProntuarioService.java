package com.fadergs.salalilas.backend.medicalrecord.psychosocial.service;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.common.enums.TipoEventoTimeline;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.dto.ProntuarioRequest;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.dto.ProntuarioResponse;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.entity.Prontuario;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.repository.ProntuarioRepository;
import com.fadergs.salalilas.backend.timeline.service.TimelineService;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import com.fadergs.salalilas.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProntuarioService {
    private final ProntuarioRepository prontuarioRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TimelineService timelineService;

    @Transactional(readOnly = true)
    public ProntuarioResponse buscar(UUID agendamentoId) {
        Prontuario prontuario = prontuarioRepository
                .findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.FORM_PRONTUARIO_NOT_FOUND));
        return toResponse(prontuario);
    }

    @Transactional
    public ProntuarioResponse criar(UUID agendamentoId,
                                    ProntuarioRequest request,
                                    UUID usuarioId) {
        Agendamento agendamento = findAgendamentoOrThrow(agendamentoId);
        Usuario usuario = findUsuarioOrThrow(usuarioId);

        if (agendamento.getStatus() != StatusAtendimento.PSICOLOGIA) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        if (prontuarioRepository.existsByAgendamentoId(agendamentoId)) {
            throw new BusinessException(ErrorCode.FORM_PRONTUARIO_EXISTS);
        }

        Prontuario prontuario = Prontuario.builder()
                .agendamento(agendamento)
                .observacoesPsicossociais(request.observacoesPsicossocias())
                .criadoPor(usuario)
                .build();

        prontuarioRepository.save(prontuario);

        timelineService.registrar(
                agendamento,
                TipoEventoTimeline.PRONTUARIO_REGISTRADO,
                "Prontuário psicossocial registrado por " + usuario.getNome(),
                usuario
        );

        log.info("Prontuário created for appointment {}", agendamentoId);
        return toResponse(prontuario);
    }

    @Transactional
    public ProntuarioResponse atualizar(UUID agendamentoId,
                                        ProntuarioRequest request,
                                        UUID usuarioId) {
        Prontuario prontuario = prontuarioRepository
                .findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.FORM_PRONTUARIO_NOT_FOUND));

        if (prontuario.getAgendamento().getStatus() != StatusAtendimento.PSICOLOGIA) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        prontuario.setObservacoesPsicossociais(request.observacoesPsicossocias());
        prontuarioRepository.save(prontuario);
        return toResponse(prontuario);
    }

    public ProntuarioResponse toResponse(Prontuario p) {
        return new ProntuarioResponse(
                p.getId(),
                p.getAgendamento().getId(),
                p.getAgendamento().getPaciente().getNome(),
                p.getObservacoesPsicossociais(),
                p.getCriadoPor().getNome(),
                p.getCriadoEm()
        );
    }

    private Agendamento findAgendamentoOrThrow(UUID id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AGD_NOT_FOUND));
    }

    private Usuario findUsuarioOrThrow(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USR_NOT_FOUND));
    }
}
