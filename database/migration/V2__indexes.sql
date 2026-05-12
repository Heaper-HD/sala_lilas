-- =================================
-- INDEXES
-- =================================
-- Query mais frequente: fetch agendamentos pelo status
CREATE INDEX idx_agendamentos_status ON agendamentos(status);
-- Dashboard: agendamentos por data
CREATE INDEX idx_agendamentos_data ON agendamentos(data);
-- Combinado: file de endpoints sempre filtrado por status E data
CREATE INDEX idx_agendamentos_status_data ON agendamentos(status, data);
-- Procura de paciente por CPF
CREATE INDEX idx_pacientes_cpf ON pacientes(cpf);
-- Timeline sempre consuldata para agendamentos, ordenada por tempo
CREATE INDEX idx_timeline_agendamento ON encaminhamentos(agendamento_id);
-- Encaminhamentos por agendamento
CREATE INDEX idx_encaminhamentos_agendamento ON encaminhamentos(agendamento_id);
-- Procura de usuario por email (login)
CREATE INDEX idx_usuarios_email ON usuarios(email);