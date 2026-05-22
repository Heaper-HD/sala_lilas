package com.fadergs.salalilas.backend.report.dto;

import java.time.LocalDate;

public record VolumeDiarioResponse(
        LocalDate data,
        long total
) {
}
