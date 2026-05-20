package com.fadergs.salalilas.backend.appointment.dto;

import java.time.LocalTime;
import java.util.List;

public record HorariosDisponiveisResponse(
        List<LocalTime> horarios
) {
}
