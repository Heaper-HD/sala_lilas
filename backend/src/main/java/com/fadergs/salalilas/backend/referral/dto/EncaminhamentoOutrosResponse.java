package com.fadergs.salalilas.backend.referral.dto;

import java.util.UUID;

public record EncaminhamentoOutrosResponse(
        UUID agendamentoId,
        String status,
        String pdfUrl
) {
}
