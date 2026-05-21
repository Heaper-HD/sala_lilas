package com.fadergs.salalilas.backend.referral.repository;

import com.fadergs.salalilas.backend.referral.entity.Encaminhamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EncaminhamentoRepository extends JpaRepository<Encaminhamento, UUID> {
    List<Encaminhamento> findByAgendamentoIdOrderByCriadoEmAsc(UUID agendamentoId);

    Optional<Encaminhamento> findTopByAgendamentoIdOrderByCriadoEmDesc(UUID agendamentoId);
}
