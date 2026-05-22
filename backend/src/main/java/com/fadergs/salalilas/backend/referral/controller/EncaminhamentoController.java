package com.fadergs.salalilas.backend.referral.controller;

import com.fadergs.salalilas.backend.common.enums.PerfilUsuario;
import com.fadergs.salalilas.backend.referral.dto.EncaminhamentoOutrosResponse;
import com.fadergs.salalilas.backend.referral.dto.EncaminhamentoResponse;
import com.fadergs.salalilas.backend.referral.service.EncaminhamentoService;
import com.fadergs.salalilas.backend.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/encaminhamentos")
@RequiredArgsConstructor
@Tag(name = "Encaminhamentos", description = "Roteamento de atendimentos entre setores")
public class EncaminhamentoController {
    private final EncaminhamentoService encaminhamentoService;

    @PostMapping("/{id}/tecnica")
    @PreAuthorize("hasAnyAuthority('ATENDENTE', 'CIS', 'NPJ')")
    @Operation(summary = "Encaminha para Equipe Técnica")
    public ResponseEntity<EncaminhamentoResponse> paraTecnica(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                encaminhamentoService.encaminhar(id, PerfilUsuario.TECNICA, usuarioId));
    }

    @PostMapping("/{id}/psicologia")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'NPJ')")
    @Operation(summary = "Encaminha para CIS (Psicologia)")
    public ResponseEntity<EncaminhamentoResponse> paraPsicologia(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                encaminhamentoService.encaminhar(id, PerfilUsuario.CIS, usuarioId));
    }

    @PostMapping("/{id}/juridico")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'CIS')")
    @Operation(summary = "Encaminha para NPJ (Jurídico)")
    public ResponseEntity<EncaminhamentoResponse> paraJuridico(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(
                encaminhamentoService.encaminhar(id, PerfilUsuario.NPJ, usuarioId));
    }

    @PostMapping("/{id}/outros")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'CIS', 'NPJ')")
    @Operation(summary = "Encaminha para Outros - finaliza atendimento e gera PDF")
    public ResponseEntity<EncaminhamentoOutrosResponse> paraOutros(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(encaminhamentoService.encaminharOutros(id, usuarioId));
    }

    @PostMapping("/{id}/finalizar")
    @PreAuthorize("hasAnyAuthority('TECNICA')")
    @Operation(summary = "Finaliza atendimento sem encaminhar - uso exclusivo da Equipe Técnica")
    public ResponseEntity<EncaminhamentoResponse> finalizar(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(encaminhamentoService.finalizar(id, usuarioId));
    }
}
