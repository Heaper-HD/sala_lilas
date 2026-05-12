package com.fadergs.salalilas.backend.timeline.repository;

import com.fadergs.salalilas.backend.timeline.entity.Timeline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface TimelineRepository extends JpaRepository<Timeline, UUID> {
    List<Timeline> findByAgendamentoIdOrderByCriadoEmAsc(UUID agendamentoId);
}
