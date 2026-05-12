package com.fadergs.salalilas.backend.patient.repository;

import com.fadergs.salalilas.backend.patient.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    Optional<Paciente> findByCpf(String cpf);

    Page<Paciente> findByNomeContainingIgnoreCaseOrCpfContaining(
            String nome, String cpf, Pageable pageable
    );
}
