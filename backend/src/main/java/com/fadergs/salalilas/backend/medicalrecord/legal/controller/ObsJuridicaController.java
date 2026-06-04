package com.fadergs.salalilas.backend.medicalrecord.legal.controller;

import com.fadergs.salalilas.backend.medicalrecord.legal.dto.ObsJuridicaRequest;
import com.fadergs.salalilas.backend.medicalrecord.legal.dto.ObsJuridicaResponse;
import com.fadergs.salalilas.backend.medicalrecord.legal.service.ObsJuridicaService;
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
@RequestMapping("/obs-juridicas")
@RequiredArgsConstructor
@Tag(name = "Observacao Juridica", description = "Preenchida pelo NPJ - leitura Técnita, CIS e Admin")
public class ObsJuridicaController {
    private final ObsJuridicaService service;

    @GetMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'CIS', 'NPJ', 'ADMIN')")
    @Operation(summary = "Busca observação jurídica de um atendimento")
    public ResponseEntity<ObsJuridicaResponse> buscar(
            @PathVariable UUID agendamentoId) {
        return ResponseEntity.ok(service.buscar(agendamentoId));
    }

    @PostMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('NPJ')")
    @Operation(summary = "Cria observação jurídica — somente NPJ, status deve ser JURIDICO")
    public ResponseEntity<ObsJuridicaResponse> criar(
            @PathVariable UUID agendamentoId,
            @Valid @RequestBody ObsJuridicaRequest request) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(agendamentoId, request, usuarioId));
    }

    @PutMapping("/{agendamentoId}")
    @PreAuthorize("hasAnyAuthority('NPJ')")
    @Operation(summary = "Atualiza observação jurídica — somente enquanto status = JURIDICO")
    public ResponseEntity<ObsJuridicaResponse> atualizar(
            @PathVariable UUID agendamentoId,
            @Valid @RequestBody ObsJuridicaRequest request) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(service.atualizar(agendamentoId, request, usuarioId));
    }
}
