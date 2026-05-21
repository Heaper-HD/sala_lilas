package com.fadergs.salalilas.backend.dashboard.controller;

import com.fadergs.salalilas.backend.appointment.dto.AgendamentoDashboardResponse;
import com.fadergs.salalilas.backend.appointment.dto.ContadoresResponse;
import com.fadergs.salalilas.backend.dashboard.service.DashboardService;
import com.fadergs.salalilas.backend.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard de reocepção e agendamento do dia")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/agendamentos")
    @PreAuthorize("hasAnyAuthority('ATENDENTE', 'ADMIN', 'TECNICA')")
    @Operation(summary = "Lista agendamentos do dia com status AGENDADO")
    public ResponseEntity<List<AgendamentoDashboardResponse>> listar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate data) {
        return ResponseEntity.ok(dashboardService.listarAgendamentos(data));
    }

    @GetMapping("/contadores")
    @PreAuthorize("hasAnyAuthority('ATENDENTE', 'ADMIN', 'TECNICA')")
    @Operation(summary = "Retorna contadores da fila do dia")
    public ResponseEntity<ContadoresResponse> contadores() {
        return ResponseEntity.ok(dashboardService.contadores());
    }

    @PatchMapping("/agendamentos/{id}/checkin")
    @PreAuthorize("hasAuthority('ATENDENTE')")
    @Operation(summary = "Check-in do paciente - altera status para TRIAGEM")
    public ResponseEntity<AgendamentoDashboardResponse> checkin(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(dashboardService.checkin(id, usuarioId));
    }

    @PatchMapping("/agendamentos/{id}/nao-veio")
    @PreAuthorize("hasAuthority('ATENDENTE')")
    @Operation(summary = "Marca paciente como ausense - finaliza atendimento (irreversível)")
    public ResponseEntity<AgendamentoDashboardResponse> naoVeio(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(dashboardService.naoVeio(id, usuarioId));
    }
}
