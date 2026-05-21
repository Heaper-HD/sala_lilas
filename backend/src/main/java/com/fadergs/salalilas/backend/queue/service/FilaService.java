package com.fadergs.salalilas.backend.queue.service;

import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.queue.dto.FilaItemResponse;
import com.fadergs.salalilas.backend.referral.entity.Encaminhamento;
import com.fadergs.salalilas.backend.referral.repository.EncaminhamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilaService {
    private final AgendamentoRepository agendamentoRepository;
    private final EncaminhamentoRepository encaminhamentoRepository;

    @Transactional
    public List<FilaItemResponse> listarFila(StatusAtendimento status, LocalDate data) {
        LocalDate targetDate = data != null ? data : LocalDate.now();

        return agendamentoRepository
                .findFilaByStatusAndData(status, targetDate)
                .stream()
                .map(a -> {
                    String encaminhadoPor = encaminhamentoRepository
                            .findTopByAgendamentoIdOrderByCriadoEmDesc(a.getId())
                            .map(e -> e.getOrigemPerfil().name())
                            .orElse(null);

                    LocalDateTime encaminhadoEm = encaminhamentoRepository
                            .findTopByAgendamentoIdOrderByCriadoEmDesc(a.getId())
                            .map(Encaminhamento::getCriadoEm)
                            .orElse(null);

                    return new FilaItemResponse(
                            a.getId(),
                            a.getPaciente().getNome(),
                            a.getHorario(),
                            encaminhadoPor,
                            encaminhadoEm,
                            a.getStatus().name()
                    );
                })
                .toList();
    }
}
