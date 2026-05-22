package com.fadergs.salalilas.backend.patient.controller;

import com.fadergs.salalilas.backend.patient.dto.PacienteDetalheResponse;
import com.fadergs.salalilas.backend.patient.dto.PacienteSummaryResponse;
import com.fadergs.salalilas.backend.patient.dto.TimelineEventoResponse;
import com.fadergs.salalilas.backend.patient.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "O Cofre - histórico completo de pacientes")
public class PacienteController {
    private final PacienteService service;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('TECNICA', 'CIS', 'NPJ', 'ADMIN')")
    @Operation(summary = "Lista todos os pacientes com histórico no sistema")
    public ResponseEntity<List<PacienteSummaryResponse>> listar(
            @RequestParam(required = false) String busca) {
        return ResponseEntity.ok(service.listar(busca));
    }

    @GetMapping("/{pacienteId}")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'CIS', 'NPJ', 'ADMIN')")
    @Operation(summary = "Retorna dados completos do paciente com todos os atendiemntos")
    public ResponseEntity<PacienteDetalheResponse> buscar(
            @PathVariable UUID pacienteId) {
        return ResponseEntity.ok(service.buscar(pacienteId));
    }

    @GetMapping("{pacienteId}/timeline")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'CIS', 'NPJ', 'ADMIN')")
    @Operation(summary = "Retorna timeline cronológica completa do paciente")
    public ResponseEntity<List<TimelineEventoResponse>> timeline(
            @PathVariable UUID pacienteId) {
        return ResponseEntity.ok(service.timeline(pacienteId));
    }
}
