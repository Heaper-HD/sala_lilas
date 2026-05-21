package com.fadergs.salalilas.backend.referral.controller;

import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;
import com.fadergs.salalilas.backend.queue.dto.FilaItemResponse;
import com.fadergs.salalilas.backend.queue.service.FilaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filas")
@RequiredArgsConstructor
@Tag(name = "Filas", description = "Filas de atendimento por setor")
public class FilaController {
    private final FilaService filaService;

    @GetMapping("/tecnica")
    @PreAuthorize("hasAnyAuthority('TECNICA', 'ADMIN')")
    @Operation(summary = "Fila da Equipe Técnica — atendimentos com status TECNICA")
    public ResponseEntity<List<FilaItemResponse>> filaTecnica(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(filaService.listarFila(StatusAtendimento.TECNICA, data));
    }

    @GetMapping("/psicologia")
    @PreAuthorize("hasAnyAuthority('CIS', 'ADMIN')")
    @Operation(summary = "Fila do CIS — atendimentos com status PSICOLOGIA")
    public ResponseEntity<List<FilaItemResponse>> filaPsicologia(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(filaService.listarFila(StatusAtendimento.PSICOLOGIA, data));
    }

    @GetMapping("/juridico")
    @PreAuthorize("hasAnyAuthority('NPJ', 'ADMIN')")
    @Operation(summary = "Fila do NPJ — atendimentos com status JURIDICO")
    public ResponseEntity<List<FilaItemResponse>> filaJuridico(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(filaService.listarFila(StatusAtendimento.JURIDICO, data));
    }
}
