package com.fadergs.salalilas.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetarSenhaRequest(
        @NotBlank(message = "Senha do administrador é orbigatória") String senhaAdmin,
        @NotBlank(message = "Senha usuario") @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres") String senhaUsuario
) {
}