package com.fadergs.salalilas.backend.appointment.controller;

import com.fadergs.salalilas.backend.appointment.dto.AgendamentoPublicoRequest;
import com.fadergs.salalilas.backend.appointment.dto.AgendamentoPublicoReponse;
import com.fadergs.salalilas.backend.appointment.dto.HorariosDisponiveisResponse;
import com.fadergs.salalilas.backend.appointment.service.AgendamentoPublicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/agendamentos/publico")
@RequiredArgsConstructor
@Tag(name = "Agendamento Publico", description = "Endpoints públicos para agendamento - sem autenticação")
public class AgendamentoPublicoController {
    private final AgendamentoPublicoService agendamentoPublicoService;

    @GetMapping("/horarios")
    @Operation(summary = "Lista horários disponíveis para uma data")
    public ResponseEntity<HorariosDisponiveisResponse> horarios(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate data ) {
        return ResponseEntity.ok(agendamentoPublicoService.listarHorariosDisponeis(data));
    }

    @PostMapping
    @Operation(summary = "Cria um novo agendamento externo")
    public ResponseEntity<AgendamentoPublicoReponse> criar(
            @Valid @RequestBody AgendamentoPublicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(agendamentoPublicoService.criar(request));
    }
}
