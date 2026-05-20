-- =================================
-- SEED: usuario admin inicial
-- senha é 'admin123' hashed com bcrypt (mudar imediatamente)
-- =================================

INSERT INTO usuarios (nome, email, senha_hash, perfil, lgpd_aceito, lgpd_data)
VALUES (
    'Administrador',
    'admin@salalilas.com',
    '$2a$12$LfQYFgM7sxKOc0GdPb6Ir.mTAH7jUvs1f8FZK2jIx2JXc70gs7Hly',
    'ADMIN',
    TRUE,
    NOW()
);