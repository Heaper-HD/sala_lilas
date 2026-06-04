package com.fadergs.salalilas.backend.report.service;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.appointment.repository.AgendamentoRepository;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.exception.ErrorCode;
import com.fadergs.salalilas.backend.exception.types.BusinessException;
import com.fadergs.salalilas.backend.report.dto.AtendimentoRelatorioResponse;
import com.fadergs.salalilas.backend.report.dto.KpiResponse;
import com.fadergs.salalilas.backend.report.dto.VolumeDiarioResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RelatorioService {
    private final AgendamentoRepository agendamentoRepository;

    @Transactional(readOnly = true)
    public KpiResponse kpis(LocalDate dataInicio, LocalDate dataFim) {
        LocalDate inicio = dataInicio != null ? dataInicio : LocalDate.now().minusMonths(1);
        LocalDate fim = dataFim != null ? dataFim : LocalDate.now();

        long total = agendamentoRepository.countByPeriodo(inicio, fim);

        Map<String, Long> porStatus = new LinkedHashMap<>();
        List<Agendamento> all = agendamentoRepository.findRelatorio(inicio, fim, null);
        for (StatusAtendimento s : StatusAtendimento.values()) {
            long count = all.stream()
                    .filter(a -> a.getStatus() == s)
                    .count();
            porStatus.put(s.name().toLowerCase(), count);
        }

        List<VolumeDiarioResponse> porDia = agendamentoRepository
                .countByDiaInPeriodo(inicio, fim)
                .stream()
                .map(row -> new VolumeDiarioResponse(
                        (LocalDate) row[0],
                        (Long) row[1]
                ))
                .toList();

        return new KpiResponse(total, porStatus, porDia);
    }

    @Transactional(readOnly = true)
    public List<AtendimentoRelatorioResponse> atendimentos(LocalDate dataInicio,
                                                           LocalDate dataFim,
                                                           String status) {
        LocalDate inicio = dataInicio != null ? dataInicio : LocalDate.now().minusMonths(1);
        LocalDate fim = dataFim != null ? dataFim : LocalDate.now();

        StatusAtendimento statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = StatusAtendimento.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException(ErrorCode.VALIDATION_INVALID_STATUS);
            }
        }

        List<Agendamento> agendamentos = statusEnum != null
                ? agendamentoRepository.findRelatorioComStatus(inicio, fim, statusEnum)
                : agendamentoRepository.findRelatorioSemFiltro(inicio, fim);

        return agendamentos.stream()
                .map(a -> new AtendimentoRelatorioResponse(
                        a.getId(),
                        a.getPaciente().getNome(),
                        a.getData(),
                        a.getHorario(),
                        a.getStatus().name(),
                        a.getAtendente() != null ? a.getAtendente().getNome() : null,
                        a.getCriadoEm()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VolumeDiarioResponse> volumeDiario(LocalDate dataInicio,
                                                   LocalDate dataFim) {
        LocalDate inicio = dataInicio != null ? dataInicio : LocalDate.now().minusMonths(1);
        LocalDate fim = dataFim != null ? dataFim : LocalDate.now();

        return agendamentoRepository.countByDiaInPeriodo(inicio, fim)
                .stream()
                .map(row -> new VolumeDiarioResponse(
                        (LocalDate) row[0],
                        (Long) row[1]
                ))
                .toList();
    }
}
