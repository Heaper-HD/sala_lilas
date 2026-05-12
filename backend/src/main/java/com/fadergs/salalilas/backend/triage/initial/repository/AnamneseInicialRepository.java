package com.fadergs.salalilas.backend.triage.initial.repository;

import com.fadergs.salalilas.backend.triage.initial.entity.AnamneseInicial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface AnamneseInicialRepository extends JpaRepository<AnamneseInicial, UUID> {
    Optional<AnamneseInicial> findByAgendamentoId(UUID agendamentoId);

    boolean existsByAgendamentoId(UUID agendamentoId);
}
