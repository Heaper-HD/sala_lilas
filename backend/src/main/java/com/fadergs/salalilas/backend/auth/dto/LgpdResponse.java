package com.fadergs.salalilas.backend.auth.dto;

import java.time.OffsetDateTime;

public record LgpdResponse(
        boolean lgpdAceito,
        OffsetDateTime lgpdData
) {
}
