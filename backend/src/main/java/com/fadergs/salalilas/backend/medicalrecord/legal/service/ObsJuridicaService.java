package com.fadergs.salalilas.backend.medicalrecord.legal.service;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.common.enums.TipoEventoTimeline;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import com.fadergs.salalilas.backend.medicalrecord.legal.dto.ObsJuridicaRequest;
import com.fadergs.salalilas.backend.medicalrecord.legal.dto.ObsJuridicaResponse;
import com.fadergs.salalilas.backend.medicalrecord.legal.entity.ObsJuridica;
import com.fadergs.salalilas.backend.medicalrecord.legal.repository.ObsJuridicaRepository;
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
public class ObsJuridicaService {
    private final ObsJuridicaRepository obsJuridicaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TimelineService timelineService;

    @Transactional(readOnly = true)
    public ObsJuridicaResponse buscar(UUID agendamentoId) {
        ObsJuridica obs = obsJuridicaRepository
                .findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.FORM_OBS_JURIDICA_NOT_FOUND));
        return toResponse(obs);
    }

    @Transactional
    public ObsJuridicaResponse criar(UUID agendamentoId,
                                     ObsJuridicaRequest request,
                                     UUID usuarioId) {
        Agendamento agendamento = findAgendamentoOrThrow(agendamentoId);
        Usuario usuario = findUsuarioOrThrow(usuarioId);

        if (agendamento.getStatus() != StatusAtendimento.JURIDICO) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        if (obsJuridicaRepository.existsByAgendamentoId(agendamentoId)) {
            throw new BusinessException(ErrorCode.FORM_OBS_JURIDICA_EXISTS);
        }

        ObsJuridica obs = ObsJuridica.builder()
                .agendamento(agendamento)
                .encaminhamentosLegais(request.encaminhamentosLegais())
                .criadoPor(usuario)
                .build();

        obsJuridicaRepository.save(obs);

        timelineService.registrar(
                agendamento,
                TipoEventoTimeline.OBS_JURIDICA_REGISTRADA,
                "Observação jurídica registrada por " + usuario.getNome(),
                usuario
        );

        log.info("Observação jurídica created for appointment {}", agendamentoId);
        return toResponse(obs);
    }

    @Transactional
    public ObsJuridicaResponse atualizar(UUID agendamnetoId,
                                         ObsJuridicaRequest request,
                                         UUID usuarioId) {
        ObsJuridica obs = obsJuridicaRepository
                .findByAgendamentoId(agendamnetoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.FORM_OBS_JURIDICA_NOT_FOUND));

        if (obs.getAgendamento().getStatus() != StatusAtendimento.JURIDICO) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        obs.setEncaminhamentosLegais(request.encaminhamentosLegais());
        obsJuridicaRepository.save(obs);
        return toResponse(obs);
    }

    public ObsJuridicaResponse toResponse(ObsJuridica o) {
        return new ObsJuridicaResponse(
                o.getId(),
                o.getAgendamento().getId(),
                o.getAgendamento().getPaciente().getNome(),
                o.getEncaminhamentosLegais(),
                o.getCriadoPor().getNome(),
                o.getCriadoEm()
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
