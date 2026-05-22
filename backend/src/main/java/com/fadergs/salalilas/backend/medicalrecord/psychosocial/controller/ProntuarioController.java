package com.fadergs.salalilas.backend.medicalrecord.psychosocial.controller;

import com.fadergs.salalilas.backend.medicalrecord.psychosocial.dto.ProntuarioRequest;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.dto.ProntuarioResponse;
import com.fadergs.salalilas.backend.medicalrecord.psychosocial.service.ProntuarioService;
import com.fadergs.salalilas.backend.security.SecurityUtils;
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
@RequestMapping("/prontuarios")
@RequiredArgsConstructor
@Tag(name = "Prontuario Psicossocial", description = "Preenchido pelo CIS - leitura para Técnica, NPJ e Admin")
public class ProntuarioController {
    private final ProntuarioService service;

    @GetMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'CIS', 'NPJ', 'ADMIN')")
    @Operation(summary = "Busca prontuário psicossocial de um atendimento")
    public ResponseEntity<ProntuarioResponse> buscar(
            @PathVariable UUID agendamentoId) {
        return ResponseEntity.ok(service.buscar(agendamentoId));
    }

    @PostMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('CIS')")
    @Operation(summary = "Cria prontuário - somente CIS, status deve ser PSICOLOGIA")
    public ResponseEntity<ProntuarioResponse> criar(
            @PathVariable UUID agendamentoId,
            @Valid @RequestBody ProntuarioRequest request) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(agendamentoId, request, usuarioId));
    }

    @PutMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('CIS')")
    @Operation(summary = "Atualiza prontuário - somente enquanto status = PSICOLOGIA")
    public ResponseEntity<ProntuarioResponse> atualizar(
            @PathVariable UUID agendamentoId,
            @Valid @RequestBody ProntuarioRequest request) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.atualizar(agendamentoId, request, usuarioId));
    }
}
