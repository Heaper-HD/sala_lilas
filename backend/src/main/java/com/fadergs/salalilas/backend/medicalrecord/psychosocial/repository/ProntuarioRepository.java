package com.fadergs.salalilas.backend.medicalrecord.psychosocial.repository;

import com.fadergs.salalilas.backend.medicalrecord.psychosocial.entity.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProntuarioRepository extends JpaRepository<Prontuario, UUID> {
    Optional<Prontuario> findByAgendamentoId(UUID agendamentoId);

    boolean existsByAgendamentoId(UUID agendamentoId);
}
