package com.fadergs.salalilas.backend.triage.technical.controller;

import com.fadergs.salalilas.backend.security.SecurityUtils;
import com.fadergs.salalilas.backend.triage.technical.dto.AnamneseTecnicaRequest;
import com.fadergs.salalilas.backend.triage.technical.dto.AnamneseTecnicaResponse;
import com.fadergs.salalilas.backend.triage.technical.service.AnamneseTecnicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/anamnese-tecnica")
@RequiredArgsConstructor
@Tag(name = "Anamnese Tecnica", description = "Preenchida pela Equipe Técnica - Atendente não tem acesso")
public class AnamneseTecnicaController {
    private final AnamneseTecnicaService service;

    @GetMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'CIS', 'NPJ', 'ADMIN')")
    @Operation(summary = "Busca anamnese técnica - ATENDENTE recebe 403")
    public ResponseEntity<AnamneseTecnicaResponse> buscar(
            @PathVariable UUID agendamentoId) {
        return ResponseEntity.ok(service.buscar(agendamentoId));
    }

    @PostMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('TECNICA')")
    @Operation(summary = "Cria anamnese técnica - somente equipe Técnica, status deve ser TECNICA")
    public ResponseEntity<AnamneseTecnicaResponse> criar(
            @PathVariable UUID agendamentoId,
            @Valid @RequestBody AnamneseTecnicaRequest request) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(agendamentoId, request, usuarioId));
    }

    @PutMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('TECNICA')")
    @Operation(summary = "Atualiza anamnese técnica - somente quando status = TECNICA")
    public ResponseEntity<AnamneseTecnicaResponse> atualizar(
            @PathVariable UUID agendamentoId,
            @Valid @RequestBody AnamneseTecnicaRequest request) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.atualizar(agendamentoId, request, usuarioId));
    }
}
