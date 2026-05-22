package com.fadergs.salalilas.backend.triage.initial.controller;

import com.fadergs.salalilas.backend.security.SecurityUtils;
import com.fadergs.salalilas.backend.triage.initial.dto.AnamneseInicialRequest;
import com.fadergs.salalilas.backend.triage.initial.dto.AnamneseInicialResponse;
import com.fadergs.salalilas.backend.triage.initial.service.AnamneseInicialService;
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
@RequestMapping("/anamnese-inicial")
@RequiredArgsConstructor
@Tag(name = "Anamnese Inicial", description = "Preenchida pelo Atendente após check-in")
public class AnamneseInicialController {
    private final AnamneseInicialService service;

    @GetMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('ATENDENTE', 'TECNICA', 'CIS', 'NPJ', 'ADMIN')")
    @Operation(summary = "Busca anamnese inicial de um atendimento")
    public ResponseEntity<AnamneseInicialResponse> buscar(
            @PathVariable UUID agendamentoId) {
        return ResponseEntity.ok(service.buscar(agendamentoId));
    }

    @PostMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('ATENDENTE')")
    @Operation(summary = "Cria anamnese inicial - somente Atendente, status deve ser TRIAGEM")
    public ResponseEntity<AnamneseInicialResponse> criar(
            @PathVariable UUID agendamentoId,
            @Valid @RequestBody AnamneseInicialRequest request) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(agendamentoId, request, usuarioId));
    }

    @PutMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('ATENDENTE')")
    @Operation(summary = "Atualiza anamnese inicial - somente enquanto status = TRIAGEM")
    public ResponseEntity<AnamneseInicialResponse> atualizar(
            @PathVariable UUID agendamentoId,
            @Valid @RequestBody AnamneseInicialRequest request) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.atualizar(agendamentoId, request, usuarioId));
    }
}
