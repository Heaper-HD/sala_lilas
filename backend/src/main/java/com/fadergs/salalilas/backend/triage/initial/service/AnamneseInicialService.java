package com.fadergs.salalilas.backend.triage.initial.service;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.common.enums.TipoEventoTimeline;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import com.fadergs.salalilas.backend.timeline.service.TimelineService;
import com.fadergs.salalilas.backend.triage.initial.dto.AnamneseInicialRequest;
import com.fadergs.salalilas.backend.triage.initial.dto.AnamneseInicialResponse;
import com.fadergs.salalilas.backend.triage.initial.dto.ViolenciaResponse;
import com.fadergs.salalilas.backend.triage.initial.entity.AnamneseInicial;
import com.fadergs.salalilas.backend.triage.initial.entity.AnamneseInicialViolencia;
import com.fadergs.salalilas.backend.triage.initial.repository.AnamneseInicialRepository;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import com.fadergs.salalilas.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnamneseInicialService {
    private final AnamneseInicialRepository anamneseInicialRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TimelineService timelineService;

    @Transactional(readOnly = true)
    public AnamneseInicialResponse buscar(UUID agendamentoId) {
        AnamneseInicial anamnese = anamneseInicialRepository
                .findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FORM_ANAMNESE_INICIAL_NOT_FOUND));
        return toResponse(anamnese);
    }

    @Transactional
    public AnamneseInicialResponse criar(UUID agendamentoId,
                                         AnamneseInicialRequest request,
                                         UUID usuarioId) {
        Agendamento agendamento = findAgendamentoOrThorw(agendamentoId);
        Usuario usuario = findUsuarioOrThrow(usuarioId);

        if (agendamento.getStatus() != StatusAtendimento.TRIAGEM) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        if (anamneseInicialRepository.existsByAgendamentoId(agendamentoId)) {
            throw new BusinessException(ErrorCode.FORM_ANAMNESE_INICIAL_EXISTS);
        }

        AnamneseInicial anamnese = AnamneseInicial.builder()
                .agendamento(agendamento)
                .tipoAtendimento(request.tipoAtendimento())
                .primeiroAtendimento(request.primeiroAtendimento())
                .territorio(request.territorio())
                .corRaca(request.corRaca())
                .sexoGenero(request.sexoGenero())
                .sexoGeneroOutro(request.sexoGeneroOutro())
                .build();

        List<AnamneseInicialViolencia> violencias = request.violencias() != null
                ? request.violencias().stream()
                  .filter(v -> v.violencia() != null && !v.violencia().isBlank())
                    .map(v -> AnamneseInicialViolencia.builder()
                            .id(new AnamneseInicialViolencia.AnamneseInicialViolenciaId(
                                    null, v.violencia()))
                            .anamneseInicial(anamnese)
                            .violenciaOutro(v.violenciaOutro())
                            .build())
                    .collect(Collectors.toList())
                : new ArrayList<>();

        anamnese.setViolencias(violencias);
        AnamneseInicial saved = anamneseInicialRepository.save(anamnese);

        saved.getViolencias().forEach(v ->
                v.getId().setAnamneseInicialId(saved.getId()));

        anamneseInicialRepository.save(saved);

        timelineService.registrar(
                agendamento,
                TipoEventoTimeline.ANAMNESE_INICIAL_REGISTRADA,
                "Anamnese inicial registrada por " + usuario.getNome(),
                usuario
        );

        log.info("Anamnese inicial created for appointment {}", agendamentoId);
        return toResponse(saved);
    }

    @Transactional
    public AnamneseInicialResponse atualizar(UUID agendamentoId,
                                             AnamneseInicialRequest request,
                                             UUID usuarioId) {
        AnamneseInicial anamnese = anamneseInicialRepository
                .findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FORM_ANAMNESE_INICIAL_NOT_FOUND));

        if (anamnese.getAgendamento().getStatus() != StatusAtendimento.TRIAGEM) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        anamnese.setTipoAtendimento(request.tipoAtendimento());
        anamnese.setPrimeiroAtendimento(request.primeiroAtendimento());
        anamnese.setTerritorio(request.territorio());
        anamnese.setCorRaca(request.corRaca());
        anamnese.setSexoGenero(request.sexoGenero());
        anamnese.setSexoGeneroOutro(request.sexoGeneroOutro());

        anamnese.getViolencias().clear();
        List<AnamneseInicialViolencia> novasViolencias = request.violencias() != null
                ? request.violencias().stream()
                  .filter(v -> v.violencia() != null && !v.violencia().isBlank())
                  .map(v -> AnamneseInicialViolencia.builder()
                            .id(new AnamneseInicialViolencia.AnamneseInicialViolenciaId(
                                    anamnese.getId(), v.violencia()))
                            .anamneseInicial(anamnese)
                            .violenciaOutro(v.violenciaOutro())
                            .build())
                  .collect(Collectors.toList())
                : new ArrayList<>();

        anamnese.getViolencias().addAll(novasViolencias);

        anamneseInicialRepository.save(anamnese);
        return toResponse(anamnese);
    }

    public AnamneseInicialResponse toResponse(AnamneseInicial a) {
        Agendamento ag = a.getAgendamento();
        List<ViolenciaResponse> violencias = a.getViolencias().stream()
                .map(v -> new ViolenciaResponse(
                        v.getId().getViolencia(),
                        v.getViolenciaOutro()))
                .toList();

        return new AnamneseInicialResponse(
                a.getId(),
                ag.getId(),
                ag.getPaciente().getNome(),
                ag.getData(),
                ag.getHorario(),
                a.getTipoAtendimento(),
                a.isPrimeiroAtendimento(),
                a.getTerritorio(),
                a.getCorRaca(),
                a.getSexoGenero(),
                a.getSexoGeneroOutro(),
                violencias,
                a.getCriadoEm()
        );
    }

    private Agendamento findAgendamentoOrThorw(UUID id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.AGD_NOT_FOUND));
    }

    private Usuario findUsuarioOrThrow(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USR_NOT_FOUND));
    }
}
