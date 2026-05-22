package com.fadergs.salalilas.backend.triage.technical.repository;

import com.fadergs.salalilas.backend.triage.technical.entity.AnamneseTecnica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AnamneseTecnicaRepository extends JpaRepository<AnamneseTecnica, UUID> {
    Optional<AnamneseTecnica> findByAgendamentoId(UUID agendamentoId);

    boolean existsByAgendamentoId(UUID agendamentoId);
}
