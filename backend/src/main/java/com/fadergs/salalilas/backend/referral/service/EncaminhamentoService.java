package com.fadergs.salalilas.backend.referral.service;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.common.enums.PerfilUsuario;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.common.enums.TipoEventoTimeline;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.exception.types.ResourceNotFoundException;
import com.fadergs.salalilas.backend.referral.dto.EncaminhamentoOutrosResponse;
import com.fadergs.salalilas.backend.referral.dto.EncaminhamentoResponse;
import com.fadergs.salalilas.backend.referral.entity.Encaminhamento;
import com.fadergs.salalilas.backend.referral.repository.EncaminhamentoRepository;
import com.fadergs.salalilas.backend.referral.validator.EncaminhamentoPermissionValidator;
import com.fadergs.salalilas.backend.timeline.service.TimelineService;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import com.fadergs.salalilas.backend.user.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EncaminhamentoService {
    private final AgendamentoRepository agendamentoRepository;
    private final EncaminhamentoRepository encaminhamentoRepository;
    private final TimelineService timelineService;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public EncaminhamentoResponse encaminhar(UUID agendamentoId,
                                             PerfilUsuario destino,
                                             UUID usuarioId) {
        Agendamento agendamento = findOrThrow(agendamentoId);
        Usuario usuario = findUsuarioOrThrow(usuarioId);
        PerfilUsuario origem = usuario.getPerfil();

        if (List.of(StatusAtendimento.TECNICA, StatusAtendimento.PSICOLOGIA, StatusAtendimento.JURIDICO).contains(agendamento.getStatus())) {
            throw new BusinessException(ErrorCode.AGD_INVALID_STATUS_TRANSITION);
        }

        if (!EncaminhamentoPermissionValidator.isAllowed(origem, destino)) {
            throw new BusinessException(ErrorCode.ENC_NOT_ALLOWED);
        }

        StatusAtendimento novoStatus = EncaminhamentoPermissionValidator.statusFor(destino);
        agendamento.setStatus(novoStatus);
        agendamentoRepository.save(agendamento);

        Encaminhamento enc = Encaminhamento.builder()
                .agendamento(agendamento)
                .origemPerfil(origem)
                .destinoPerfil(destino)
                .criadoPor(usuario)
                .build();
        encaminhamentoRepository.save(enc);

        timelineService.registrar(
                agendamento,
                TipoEventoTimeline.ENCAMINHAMENTO,
                "Encaminhado de " + origem.name() + " para " + destino.name() + " por " + usuario.getNome(), usuario
        );

        log.info("Appointment {} referred from {} to {} by {}", agendamentoId, origem, destino, usuario.getEmail());

        return new EncaminhamentoResponse(
                agendamento.getId(),
                novoStatus.name(),
                origem.name(),
                destino.name(),
                enc.getCriadoEm()
        );
    }

    @Transactional
    public EncaminhamentoResponse finalizar(UUID agendamentoId, UUID usuarioId) {
        Agendamento agendamento = findOrThrow(agendamentoId);
        Usuario usuario = findUsuarioOrThrow(usuarioId);

        if (usuario.getPerfil() != PerfilUsuario.TECNICA) {
            throw new BusinessException(ErrorCode.ENC_NOT_ALLOWED);
        }

        agendamento.setStatus(StatusAtendimento.FINALIZADO);
        agendamentoRepository.save(agendamento);

        timelineService.registrar(
                agendamento,
                TipoEventoTimeline.ATENDIMENTO_FINALIZADO,
                "Atendimento finalizado sem encaminhamento por " + usuario.getNome(),
                usuario
        );

        return new EncaminhamentoResponse(
                agendamento.getId(),
                StatusAtendimento.FINALIZADO.name(),
                usuario.getPerfil().name(),
                null,
                LocalDateTime.now()
        );
    }

    @Transactional
    public EncaminhamentoOutrosResponse encaminharOutros(UUID agendamentoId, UUID usuarioId) {
        Agendamento agendamento = findOrThrow(agendamentoId);
        Usuario usuario = findUsuarioOrThrow(usuarioId);
        PerfilUsuario perfil = usuario.getPerfil();

        if (perfil != PerfilUsuario.TECNICA && perfil != PerfilUsuario.CIS && perfil != PerfilUsuario.NPJ) {
            throw new BusinessException(ErrorCode.ENC_NOT_ALLOWED);
        }

        agendamento.setStatus(StatusAtendimento.FINALIZADO);
        agendamentoRepository.save(agendamento);

        Encaminhamento enc = Encaminhamento.builder()
                .agendamento(agendamento)
                .origemPerfil(perfil)
                .destinoPerfil(null)
                .destinoOutros(true)
                .criadoPor(usuario)
                .build();
        encaminhamentoRepository.save(enc);

        timelineService.registrar(
                agendamento,
                TipoEventoTimeline.ATENDIMENTO_FINALIZADO,
                "Encaminhado para Outros e finalizado por " + usuario.getNome(),
                usuario
        );

        String pdfUrl = "/api/v1/pdf/" + agendamento.getId();

        return new EncaminhamentoOutrosResponse(
                agendamento.getId(),
                StatusAtendimento.FINALIZADO.name(),
                pdfUrl
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
