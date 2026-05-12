-- V1__init.sql
-- =================================
-- TYPES
-- =================================
CREATE TYPE perfil_usuario AS ENUM (
    'ATENDENTE',
    'TECNICA',
    'CIS',
    'NPJ',
    'ADMIN'
);
CREATE TYPE status_atendimento AS ENUM (
    'AGENDADO',
    'TRIAGEM',
    'TECNICA',
    'PSICOLOGIA',
    'JURIDICO',
    'FINALIZADO'
);
CREATE TYPE tipo_evento_timeline AS ENUM (
    'AGENDAMENTO_CRIADO',
    'CHECKIN_REALIZADO',
    'NAO_VEIO',
    'ENCAMINHAMENTO',
    'ANAMNESE_INICIAL_REGISTRADA',
    'ANAMNESE_TECNICA_REGISTRADA',
    'PRONTUARIO_REGISTRADO',
    'OBS_JURIDICA_REGISTRADA',
    'ATENDIMENTO_FINALIZADO'
);
-- =================================
-- USUARIOS
-- =================================
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha_hash VARCHAR(255),
    perfil perfil_usuario NOT NULL,
    lgpd_aceito BOOLEAN NOT NULL DEFAULT FALSE,
    lgpd_data TIMESTAMPTZ,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- =================================
-- PACIENTES
-- =================================
CREATE TABLE pacientes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- =================================
-- AGENDAMENTOS
-- =================================
CREATE TABLE agendamentos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    paciente_id UUID NOT NULL REFERENCES pacientes(id),
    atendente_id UUID REFERENCES usuarios(id),
    data DATE NOT NULL,
    horario TIME NOT NULL,
    status status_atendimento NOT NULL DEFAULT 'AGENDADO',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    -- Previne agendamento duplicado (RN007)
    CONSTRAINT uq_agendamento_data_horario UNIQUE (data, horario)
);
-- =================================
-- ANAMNESE INICIAL
-- =================================
CREATE TABLE anamnese_inicial (
    id UUID PRIMARY KEY DEfAULT gen_random_uuid(),
    agendamento_id UUID NOT NULL UNIQUE REFERENCES agendamentos(id),
    tipo_atendimento VARCHAR(20) NOT NULL CHECK (tipo_atendimento IN ('PRESENCIAL', 'ONLINE')),
    primeiro_atendimento BOOLEAN NOT NULL,
    territorio VARCHAR(255),
    cor_raca VARCHAR(50) NOT NULL,
    sexo_genero VARCHAR(50) NOT NULL,
    sexo_genero_outro VARCHAR(255),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- Tabela de junção para cracterizacao_violencia (multi-seleção)
CREATE TABLE anamnese_inicial_violencias (
    anamnese_inicial_id UUID NOT NULL REFERENcES anamnese_inicial(id),
    violencia VARCHAR(50) NOT NULL,
    -- preenchido quando violencia = 'OUTRAS'
    violencia_outro VARCHAR(255),
    PRIMARY KEY (anamnese_inicial_id, violencia)
);
-- =================================
-- ANAMNESE TECNICA
-- =================================
CREATE TABLE anamnese_tecnica (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    agendamento_id UUID NOT NULL UNIQUE REFERENCES agendamentos(id),
    risco_iminente BOOLEAN NOT NULL,
    agressor_convive BOOLEAN NOT NULL,
    historico_violencia BOOLEAN NOT NULL,
    rede_apoio BOOLEAN NOT NULL,
    filhos_dependentes BOOLEAN NOT NULL,
    observacoes TEXT,
    registro_atendimento TEXT,
    detalhamento_encaminhamentos TEXT,
    plano_acompanhamento VARCHAR(50) NOT NULL CHECK (
        plano_acompanhamento IN (
            'RETORNO_AGENDADO',
            'ACOMPANHAMENTO_CONTINUO',
            'ENCERRAMENTO'
        )
    ),
    data_retorno DATE,
    plano_observacoes TEXT,
    sintese_caso TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- Tabela de junção para orientações realizadas (multi-seleção)
CREATE TABLE anamnese_tecnica_orientacoes (
    anamnese_tecnica_id UUID NOT NULL REFERENCES anamnese_tecnica(id),
    orientacao VARCHAR(50) NOT NULL,
    orientacao_outro VARCHAR(255),
    PRIMARY KEY (anamnese_tecnica_id, orientacao)
);
-- Tabela de junção para encaminhamentos realizados (multi-seleção)
CREATE TABLE anamnese_tecnica_encaminhamentos (
    anamnese_tecnica_id UUID NOT NULL REFERENCES anamnese_tecnica(id),
    encaminhamento VARCHAR(50) NOT NULL,
    envaminhamento_outro VARCHAR(255),
    PRIMARY KEY (anamnese_tecnica_id, encaminhamento)
);
-- Tabela de junção para objetivo do encaminhamento (multi-seleção)
CREATE TABLE anamnese_tecnica_objetivos (
    anamnese_tecnica_id UUID NOT NULL REFERENCES anamnese_tecnica(id),
    objetivo VARCHAR(50) NOT NULL,
    objetivo_outro VARCHAR(255),
    PRIMARY KEY (anamnese_tecnica_id, objetivo)
);
-- =================================
-- PRONTUARIO PSICOSSOCIAL
-- =================================
CREATE TABLE prontuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    agendamento_id UUID NOT NULL UNIQUE REFERENCES agendamentos(id),
    observacoes_psicossociais TEXT NOT NULL,
    criado_por UUID NOT NULL REFEREnCES usuarios(id),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMPTZ
);
-- =================================
-- OBSERVACAO JURIDICA
-- =================================
CREATE TABLE obs_juridicas (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    agendamento_id UUID NOT NULL UNIQUE REFERENCES agendamentos(id),
    encaminhamentos_legais TEXT NOT NULL,
    criado_por UUID NOT NULL REFERENCES usuarios(id),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMPTZ
);
-- =================================
-- ENCAMINHAMENTOS
-- =================================
CREATE TABLE encaminhamentos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    agendamento_id UUID NOT NULL REFERENCES agendamentos(id),
    origem_perfil perfil_usuario NOT NULL,
    -- nulo quando destino for 'OUTROS'
    destino_perfil perfil_usuario,
    destino_outros BOOLEAN NOT NULL DEFAULT FALSE,
    criado_por UUID NOT NULL REFERENCES usuarios(id),
    creado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
-- =================================
-- TIMELINE
-- =================================
CREATE TABLE timeline (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    agendamento_id UUID NOT NULL REFERENCES agendamentos(id),
    evento tipo_evento_timeline NOT NULL,
    descricao TEXT NOT NULL,
    -- nulo para externo (form publico)
    criado_por UUID REFERENCES usuarios(id),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
