-- =================================
-- SEED: usuario admin inicial
-- senha é 'admin123' hashed com bcrypt (mudar imediatamente)
-- =================================

INSERT INTO usuarios (nome, email, senha_hash, perfil, lgpd_aceito, lgpd_data)
VALUES (
    'Administrador',
    'admin@salalilas.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'ADMIN',
    TRUE,
    NOW()
);