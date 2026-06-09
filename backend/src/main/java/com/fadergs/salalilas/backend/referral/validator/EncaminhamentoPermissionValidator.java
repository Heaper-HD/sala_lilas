package com.fadergs.salalilas.backend.referral.validator;

import com.fadergs.salalilas.backend.common.enums.PerfilUsuario;
import com.fadergs.salalilas.backend.common.enums.StatusAtendimento;

public class EncaminhamentoPermissionValidator {
    private EncaminhamentoPermissionValidator() {}

    public static boolean isAllowed(PerfilUsuario origem, PerfilUsuario destino) {
        return switch (origem) {
            case ATENDENTE  -> destino == PerfilUsuario.TECNICA;
            case TECNICA    -> destino == PerfilUsuario.CIS
                            || destino == PerfilUsuario.NPJ;
            case CIS        -> destino == PerfilUsuario.NPJ
                            || destino == PerfilUsuario.TECNICA;
            case NPJ        -> destino == PerfilUsuario.CIS
                            || destino == PerfilUsuario.TECNICA;
            default -> false;
        };
    }

    public static StatusAtendimento statusFor(PerfilUsuario destino) {
        return switch (destino) {
            case TECNICA    -> StatusAtendimento.TECNICA;
            case CIS        -> StatusAtendimento.PSICOLOGIA;
            case NPJ        -> StatusAtendimento.JURIDICO;
            default         -> throw new IllegalArgumentException("No status mapping for perfil: " + destino);
        };
    }

    public static boolean isOwnerOfStatus(PerfilUsuario usuarioPerfil, StatusAtendimento currentStatus) {
        return switch (currentStatus) {
            case AGENDADO, TRIAGEM -> usuarioPerfil == PerfilUsuario.ATENDENTE;
            case TECNICA     -> usuarioPerfil == PerfilUsuario.TECNICA;
            case PSICOLOGIA  -> usuarioPerfil == PerfilUsuario.CIS;
            case JURIDICO    -> usuarioPerfil == PerfilUsuario.NPJ;
            default          -> false;
        };
    }
}
