package com.fadergs.salalilas.backend.medicalrecord.legal.repository;

import com.fadergs.salalilas.backend.medicalrecord.legal.entity.ObsJuridica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ObsJuridicaRepository extends JpaRepository<ObsJuridica, UUID> {
    Optional<ObsJuridica> findByAgendamentoId(UUID agendamentoId);

    boolean existsByAgendamentoId(UUID agendamentoId);
}
