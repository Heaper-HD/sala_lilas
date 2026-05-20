package com.fadergs.salalilas.backend.appointment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoPublicoRequest(
        @NotBlank(message = "Nome é obrigatório") @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres") String nome,
        @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail é inválido") String email,
        @NotBlank(message = "CPF é obrigatório") String cpf,
        @NotNull(message = "Data é obrigatória") LocalDate data,
        @NotNull(message = "Horário é orbigatório") LocalTime horario
) { }
