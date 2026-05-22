package com.fadergs.salalilas.backend.patient.repository;

import com.fadergs.salalilas.backend.patient.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    Optional<Paciente> findByCpf(String cpf);

    @Query("""
        SELECT p FROM Paciente p
            WHERE (:busca IS NULL
                OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
                OR p.cpf LIKE CONCAT('%', :busca, '%'))
            ORDER BY p.nome ASC
    """)
    List<Paciente> buscarPorNomeOuCpf(String busca);
}
