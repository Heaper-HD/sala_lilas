package com.fadergs.salalilas.backend.triage.technical.service;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.common.enums.TipoEventoTimeline;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import com.fadergs.salalilas.backend.timeline.service.TimelineService;
import com.fadergs.salalilas.backend.triage.technical.dto.*;
import com.fadergs.salalilas.backend.triage.technical.entity.AnamneseTecnica;
import com.fadergs.salalilas.backend.triage.technical.entity.AnamneseTecnicaEncaminhamento;
import com.fadergs.salalilas.backend.triage.technical.entity.AnamneseTecnicaObjetivos;
import com.fadergs.salalilas.backend.triage.technical.entity.AnamneseTecnicaOrientacao;
import com.fadergs.salalilas.backend.triage.technical.repository.AnamneseTecnicaRepository;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import com.fadergs.salalilas.backend.user.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnamneseTecnicaService {
    private final AnamneseTecnicaRepository anamneseTecnicaRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TimelineService timelineService;

    @Transactional(readOnly = true)
    public AnamneseTecnicaResponse buscar(UUID agendamentoId) {
        AnamneseTecnica anamnese = anamneseTecnicaRepository
                .findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.FORM_ANAMNESE_TECNICA_NOT_FOUND));
        return toResponse(anamnese);
    }

    @Transactional
    public AnamneseTecnicaResponse criar(UUID agendamentoId,
                                         AnamneseTecnicaRequest request,
                                         UUID usuarioId) {
        Agendamento agendamento = findAgendamentoOrThrow(agendamentoId);
        Usuario usuario = findUsuarioOrThrow(usuarioId);

        if (agendamento.getStatus() != StatusAtendimento.TECNICA) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        if (anamneseTecnicaRepository.existsByAgendamentoId(agendamentoId)) {
            throw new BusinessException(ErrorCode.FORM_ANAMNESE_TECNICA_EXISTS);
        }

        AnamneseTecnica anamnese = AnamneseTecnica.builder()
                .agendamento(agendamento)
                .riscoIminente(request.riscoIminente())
                .agressorConvive(request.agressorConvive())
                .historicoViolencia(request.historicoViolencia())
                .redeApoio(request.redeApoio())
                .filhosDependentes(request.filhosDependentes())
                .observacoes(request.observacoes())
                .registroAtendimento(request.registroAtendimento())
                .detalhamentoEncaminhamentos(request.detalhamentoEncaminhamentos())
                .planoAcompanhamento(request.planoAcompanhamento())
                .dataRetorno(request.dataRetorno())
                .planoObservacoes(request.planoObservacoes())
                .sinteseCaso(request.sinteseCaso())
                .build();

        AnamneseTecnica saved = anamneseTecnicaRepository.save(anamnese);

        if (request.orientacoes() != null) {
            request.orientacoes().stream()
                    .map(o -> AnamneseTecnicaOrientacao.builder()
                            .id(new AnamneseTecnicaOrientacao.AnamneseTecnicaOrientacaoId(
                                    saved.getId(), o.orientacao()))
                            .anamneseTecnica(saved)
                            .orientacaoOutro(o.orientacaoOutro())
                            .build())
                    .forEach(saved.getOrientacoes()::add);
        }

        if (request.encaminhamentos() != null) {
            request.encaminhamentos().stream()
                    .map(e -> AnamneseTecnicaEncaminhamento.builder()
                            .id(new AnamneseTecnicaEncaminhamento.AnamneseTecnicaEncaminhamentoId(
                                    saved.getId(), e.encaminhamento()))
                            .anamneseTecnica(saved)
                            .encaminhamentoOutro(e.encaminhamentoOutro())
                            .build())
                    .forEach(saved.getEncaminhamentos()::add);
        }

        if (request.objetivos() != null) {
            request.objetivos().stream()
                    .map(o -> AnamneseTecnicaObjetivos.builder()
                            .id(new AnamneseTecnicaObjetivos.AnamneseTecnicaObjetivoId(
                                    saved.getId(), o.objetivo()))
                            .anamneseTecnica(saved)
                            .objetivoOutro(o.objetivoOutro())
                            .build())
                    .forEach(saved.getObjetivos()::add);
        }

        anamneseTecnicaRepository.save(saved);

        timelineService.registrar(
                agendamento,
                TipoEventoTimeline.ANAMNESE_INICIAL_REGISTRADA,
                "Anamnese ténica registrada por " + usuario.getNome(),
                usuario
        );

        log.info("Anamnese técnica created for appointment {}", agendamentoId);
        return toResponse(saved);
    }

    @Transactional
    public AnamneseTecnicaResponse atualizar(UUID agendamentoId,
                                         AnamneseTecnicaRequest request,
                                         UUID usuarioId) {
        AnamneseTecnica anamnese = anamneseTecnicaRepository
                .findByAgendamentoId(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.FORM_ANAMNESE_TECNICA_NOT_FOUND));

        if (anamnese.getAgendamento().getStatus() != StatusAtendimento.TECNICA) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        anamnese.setRiscoIminente(request.riscoIminente());
        anamnese.setAgressorConvive(request.agressorConvive());
        anamnese.setHistoricoViolencia(request.historicoViolencia());
        anamnese.setRedeApoio(request.redeApoio());
        anamnese.setFilhosDependentes(request.filhosDependentes());
        anamnese.setObservacoes(request.observacoes());
        anamnese.setRegistroAtendimento(request.registroAtendimento());
        anamnese.setDetalhamentoEncaminhamentos(request.detalhamentoEncaminhamentos());
        anamnese.setPlanoAcompanhamento(request.planoAcompanhamento());
        anamnese.setPlanoObservacoes(request.planoObservacoes());
        anamnese.setSinteseCaso(request.sinteseCaso());

        anamnese.getOrientacoes().clear();
        anamnese.getEncaminhamentos().clear();
        anamnese.getObjetivos().clear();

        if (request.orientacoes() != null) {
            request.orientacoes().stream()
                    .map(o -> AnamneseTecnicaOrientacao.builder()
                            .id(new AnamneseTecnicaOrientacao.AnamneseTecnicaOrientacaoId(
                                    anamnese.getId(), o.orientacao()))
                            .anamneseTecnica(anamnese)
                            .orientacaoOutro(o.orientacaoOutro())
                            .build())
                    .forEach(anamnese.getOrientacoes()::add);
        }

        if (request.encaminhamentos() != null) {
            request.encaminhamentos().stream()
                    .map(e -> AnamneseTecnicaEncaminhamento.builder()
                            .id(new AnamneseTecnicaEncaminhamento.AnamneseTecnicaEncaminhamentoId(
                                    anamnese.getId(), e.encaminhamento()))
                            .anamneseTecnica(anamnese)
                            .encaminhamentoOutro(e.encaminhamentoOutro())
                            .build())
                    .forEach(anamnese.getEncaminhamentos()::add);
        }

        if (request.objetivos() != null) {
            request.objetivos().stream()
                    .map(o -> AnamneseTecnicaObjetivos.builder()
                            .id(new AnamneseTecnicaObjetivos.AnamneseTecnicaObjetivoId(
                                    anamnese.getId(), o.objetivo()))
                            .anamneseTecnica(anamnese)
                            .objetivoOutro(o.objetivoOutro())
                            .build())
                    .forEach(anamnese.getObjetivos()::add);
        }

        anamneseTecnicaRepository.save(anamnese);
        return toResponse(anamnese);
    }


    public AnamneseTecnicaResponse toResponse(AnamneseTecnica a) {
        Agendamento ag = a.getAgendamento();

        List<OrientacaoResponse> orientacoes = a.getOrientacoes().stream()
                .map(o -> new OrientacaoResponse(
                        o.getId().getOrientacao(), o.getOrientacaoOutro()))
                .toList();

        List<EncaminhamentoTecnicoResponse> encaminhamentos = a.getEncaminhamentos().stream()
                .map(e -> new EncaminhamentoTecnicoResponse(
                        e.getId().getEncaminhamento(), e.getEncaminhamentoOutro()))
                .toList();

        List<ObjetivoResponse> objetivos = a.getObjetivos().stream()
                .map(o -> new ObjetivoResponse(
                        o.getId().getObjetivo(), o.getObjetivoOutro()))
                .toList();

        return new AnamneseTecnicaResponse(
                a.getId(),
                ag.getId(),
                ag.getPaciente().getNome(),
                a.isRiscoIminente(),
                a.isAgressorConvive(),
                a.isHistoricoViolencia(),
                a.isRedeApoio(),
                a.isFilhosDependentes(),
                a.getObservacoes(),
                a.getRegistroAtendimento(),
                a.getDetalhamentoEncaminhamentos(),
                a.getPlanoAcompanhamento(),
                a.getDataRetorno(),
                a.getPlanoObservacoes(),
                a.getSinteseCaso(),
                orientacoes,
                encaminhamentos,
                objetivos,
                a.getCriadoEm()
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
