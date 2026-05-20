package com.fadergs.salalilas.backend.user.dto;

import com.fadergs.salalilas.backend.common.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUsuarioRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
        @NotNull(message = "Perfil é obrigatório")PerfilUsuario perfil
        ) {
}
