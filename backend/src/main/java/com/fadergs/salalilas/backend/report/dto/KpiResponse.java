package com.fadergs.salalilas.backend.report.dto;

import java.util.List;
import java.util.Map;

public record KpiResponse(
        long total,
        Map<String, Long> porStatus,
        List<VolumeDiarioResponse> porDia
) {
}
