package com.fadergs.salalilas.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // --- Auth (AUTH) ---
    AUTH_INVALID_CREDENTIALS("AUTH_001", "Credenciais inválidas"),
    AUTH_TOKEN_EXPIRED("AUTH_002", "Token expirado"),
    AUTH_TOKEN_INVALID("AUTH_003", "Token inválido"),
    AUTH_LGPD_PENDING("AUTH_004", "Termo de responsabilidade pendente de aceite"),
    AUTH_OAUTH2_ACCOUNT_NOT_FOUND("AUTH_005", "Conta não encontrada. Contate o administrador"),
    AUTH_USER_DISABLED("AUTH_006", "Usuário desativado"),

    // --- Agendamento (AGD) ---
    AGD_NOT_FOUND("AGD_001", "Agendamento não encontrado"),
    AGD_SLOT_UNAVAILABLE("AGD_002", "Horário indisponível"),
    AGD_INVALID_DATE("AGD_003", "Data inválida. Somente dias úteis são permitidos"),
    AGD_INVALID_TIME("AGD_004", "Horário inváldio"),
    AGD_INVALID_CPF("AGD_005", "CPF inválido"),
    AGD_INVALID_STATUS_TRANSITION("AGD_006", "Transição de status inválida"),
    AGD_ALREADY_FINALIZED("AGD_007", "Atendimento já finalizado"),

    // --- Paciente (PAC) ---
    PAC_NOT_FOUND("PAC_001", "Paciente não encontrado"),
    PAC_INVALID_CPF("PAC_002", "CPF inválido"),

    // --- Encaminhamento (ENC) ---
    ENC_NOT_ALLOWED("ENC_001", "Encaminhamento não permitido para este perfil"),
    ENC_INVALID_DESTINATION("ENC_002", "Destino de encaminhamento inválido"),

    // --- Formulários (FORM) ---
    FORM_ANAMNESE_INICIAL_NOT_FOUND("FORM_001", "Anamnese inicial não encontrada"),
    FORM_ANAMNESE_TECNICA_NOT_FOUND("FORM_002", "Anamnese técnica não encontrada"),
    FORM_PRONTUARIO_NOT_FOUND("FORM_003", "Prontuário psicossocial não encontrado"),
    FORM_OBS_JURIDICA_NOT_FOUND("FORM_004", "Observação jurídica não encontrada"),
    FORM_WRONG_STATUS("FORM_005", "Status do atendimento não permite esta operação"),

    // --- Usuário (USR) ---
    USR_NOT_FOUND("USR_001", "Usuário não encontrado"),
    USR_EMAIL_ALREADY_EXISTS("USR_002", "E-mail já cadastrado"),
    USR_ALREADY_DISABLED("USR_003", "Usuário já está desativado"),

    // --- PDF (PDF) ---
    PDF_GENERATION_FAILED("PDF_001", "Falha ao gerar PDF"),
    PDF_NOT_FOUND("PDF_002", "PDF não encontrado para este atendimento"),

    // --- Generic ---
    VALIDATION_ERROR("VAL_001", "Erro de validação nos campos enviados"),
    ACCESS_DENIED("SEC_001", "Acesso negado"),
    INTERNAL_ERROR("ERR_001", "Erro interno. Contate o suporte");

    private final String code;
    private final String defaultMessage;
}
