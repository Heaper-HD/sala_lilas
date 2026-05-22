package com.fadergs.salalilas.backend.patient.dto;

import java.util.List;
import java.util.UUID;

public record PacienteDetalheResponse(
        UUID pacienteId,
        String nome,
        String cpf,
        String email,
        List<AtendimentoDetalheResponse> atendimentos
) {
}
