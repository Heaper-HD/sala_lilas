package com.fadergs.salalilas.backend.appointment.repository;

import com.fadergs.salalilas.backend.appointment.entity.Agendamento;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID> {
    List<Agendamento> findByDataAndStatusOrderByHorarioAsc(
            LocalDate data, StatusAtendimento status
    );

    long countByDataAndStatus(LocalDate data, StatusAtendimento status);

    boolean existsByDataAndHorario(LocalDate data, LocalTime horario);
    
    @Query("SELECT a.horario FROM Agendamento a WHERE a.data = :data")
    List<LocalTime> findHorariosOcupadosByData(@Param("data") LocalDate data);

    List<Agendamento> findByStatusAndDataOrderByHorarioAsc(
            StatusAtendimento status, LocalDate data
    );
    
    @Query("""
        SELECT a FROM Agendamento a
        WHERE a.data BETWEEN :inicio AND :fim
        AND (:status IS NULL OR a.status = :status)
        ORDER BY a.data ASC, a.horario ASC
    """)
    List<Agendamento> findByPeriodoAndStatus(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim,
            @Param("status") StatusAtendimento status
    );
    
    @Query("""
        SELECT a.status, COUNT(a) FROM Agendamento a
        WHERE a.data BETWEEN :inicio AND :fim
        GROUP BY a.status
    """)
    List<Object[]> contByStatusInPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );
    
    @Query("""
        SELECT a.data, COUNT(a) FROM Agendamento a
        WHERE a.data BETWEEN :inicio AND :fim
        GROUP BY a.data
        ORDER BY a.data ASC
    """)
    List<Object[]> countByDiaInPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    List<Agendamento> findByPacienteIdOrderByDataDescHorarioDesc(UUID pacienteId);
}
