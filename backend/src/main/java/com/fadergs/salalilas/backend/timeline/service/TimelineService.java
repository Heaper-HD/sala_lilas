package com.fadergs.salalilas.backend.timeline.service;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.common.enums.TipoEventoTimeline;
import com.fadergs.salalilas.backend.timeline.entity.Timeline;
import com.fadergs.salalilas.backend.timeline.repository.TimelineRepository;
import com.fadergs.salalilas.backend.user.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimelineService {
    private final TimelineRepository timelineRepository;

    public void registrar(Agendamento agendamento, TipoEventoTimeline evento,
                          String descricao, Usuario responsavel) {
        Timeline entry = Timeline.builder()
                .agendamento(agendamento)
                .evento(evento)
                .descricao(descricao)
                .criadoPor(responsavel)
                .build();

        timelineRepository.save(entry);
    }
}
