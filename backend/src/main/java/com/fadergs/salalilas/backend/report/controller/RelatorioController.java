package com.fadergs.salalilas.backend.report.controller;

import com.fadergs.salalilas.backend.report.dto.AtendimentoRelatorioResponse;
import com.fadergs.salalilas.backend.report.dto.KpiResponse;
import com.fadergs.salalilas.backend.report.dto.VolumeDiarioResponse;
import com.fadergs.salalilas.backend.report.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatorios", description = "Dashboard BI - acesso exclusivo do Administrador")
public class RelatorioController {
    private final RelatorioService relatorioService;

    @GetMapping("/kpis")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "KPIS gerais - total de atendimentos, por status e por dia")
    public ResponseEntity<KpiResponse> kpis(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataFim) {
        return ResponseEntity.ok(relatorioService.kpis(dataInicio, dataFim));
    }

    @GetMapping("/atendimentos")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Lista detalhada de atendimentos no período")
    public ResponseEntity<List<AtendimentoRelatorioResponse>> atendimentos(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInico,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(
                relatorioService.atendimentos(dataInico, dataFim, status));
    }

    @GetMapping("/volume-diario")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Volume de atendimentos agrupado por dia")
    public ResponseEntity<List<VolumeDiarioResponse>> volumeDiario(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInico,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return ResponseEntity.ok(relatorioService.volumeDiario(dataInico, dataFim));
    }
}