-- Setup das extenções e funções
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE OR REPLACE FUNCTION update_updated_at_column() RETURNS TRIGGER AS $$ BEGIN NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';
-- Definição das ENUMs
CREATE TYPE perfil_usuario AS ENUM ('ATENDENTE', 'NPJ', 'PSICO', 'ET');
CREATE TYPE tipo_agendamento AS ENUM ('PUBLICO', 'INTERNO');
CREATE TYPE status_agendamento AS ENUM (
    'PENDENTE',
    'CONFIRMADO',
    'CANCELADO',
    'REALIZADO'
);
CREATE TYPE status_prontuario AS ENUM ('RASCUNHO', 'ENVIADO', 'VALIDADO');
CREATE TYPE destino_encaminhamento AS ENUM ('NPJ', 'PSICO', 'ET');
CREATE TYPE status_encaminhamento AS ENUM ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDO');
-- Criação das tabelas
CREATE TABLE usuario (
    usuario_id UUID PRIMARY KEY DEFAULT uuidv7(),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    PERFIL perfil_usuario NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE agendamento (
    agendamento_id UUID PRIMARY KEY DEFAULT uuidv7(),
    nome_solicitante VARCHAR(100) NOT NULL,
    telefone_solicitante VARCHAR(100) NOT NULL,
    email_solicitante VARCHAR(100),
    tipo tipo_agendamento NOT NULL,
    status status_agendamento DEFAULT 'PENDENTE',
    observacoes TEXT,
    usuario_responsavel_id UUID REFERENCES usuario(usuario_id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE prontuario (
    prontuario_id UUID PRIMARY KEY DEFAULT uuidv7(),
    agendamento_id UUID REFERENCES agendamento(agendamento_id),
    usuario_criador_id UUID REFERENCES usuario(usuario_id),
    status status_prontuario DEFAULT 'RASCUNHO',
    descricao_caso TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE historico_prontuario (
    historico_id UUID PRIMARY KEY DEFAULT uuidv7(),
    prontuario_id UUID REFERENCES prontuario(prontuario_id),
    usuario_id UUID REFERENCES usuario(usuario_id),
    descricao_alteracao TEXT NOT NULL,
    dados_anteriores JSONB NOT NULL,
    dados_novos JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE encaminhamento (
    encaminhamento_id UUID PRIMARY KEY DEFAULT uuidv7(),
    prontuario_id UUID REFERENCES prontuario(prontuario_id),
    usuario_origem_id UUID REFERENCES usuario(usuario_id),
    usuario_destino_id UUID REFERENCES usuario(usuario_id),
    area_destino destino_encaminhamento NOT NULL,
    status status_encaminhamento DEFAULT 'PENDENTE',
    observacoes TEXT,
    feedback TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
-- Aplicação dos triggers
CREATE TRIGGER update_usuario_modtime BEFORE
UPDATE ON usuario FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_agendamento_modtime BEFORE
UPDATE ON agendamento FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_prontuario_modtime BEFORE
UPDATE ON prontuario FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_encaminhamento_modtime BEFORE
UPDATE ON encaminhamento FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();