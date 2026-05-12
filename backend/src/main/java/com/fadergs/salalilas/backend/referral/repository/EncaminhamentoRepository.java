package com.fadergs.salalilas.backend.referral.repository;

import com.fadergs.salalilas.backend.referral.entity.Encaminhamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface EncaminhamentoRepository extends JpaRepository<Encaminhamento, UUID> {
    List<Encaminhamento> findByAgendamentoIdOrderByCriadoEmAsc(UUID agendamentoId);
}
