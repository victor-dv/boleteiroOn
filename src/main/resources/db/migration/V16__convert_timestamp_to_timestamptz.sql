-- Converter colunas TIMESTAMP para TIMESTAMPTZ (com timezone)
-- O PostgreSQL assumirá que os valores existentes estão no timezone do servidor (UTC/EST)
-- e fará a conversão automática para UTC armazenado

-- 1. arrematacoes.data_arrematacao
ALTER TABLE arrematacoes 
    ALTER COLUMN data_arrematacao TYPE TIMESTAMPTZ 
    USING data_arrematacao AT TIME ZONE 'UTC';

-- 2. tokens_validacao_arrematante.data_expiracao
ALTER TABLE tokens_validacao_arrematante 
    ALTER COLUMN data_expiracao TYPE TIMESTAMPTZ 
    USING data_expiracao AT TIME ZONE 'UTC';

-- 3. users.token_expiration
ALTER TABLE users 
    ALTER COLUMN token_expiration TYPE TIMESTAMPTZ 
    USING token_expiration AT TIME ZONE 'UTC';

-- 4. documentos_auditoria.data_envio
ALTER TABLE documentos_auditoria 
    ALTER COLUMN data_envio TYPE TIMESTAMPTZ 
    USING data_envio AT TIME ZONE 'UTC';

-- Atualizar defaults para usar timezone-aware functions
ALTER TABLE arrematacoes 
    ALTER COLUMN data_arrematacao SET DEFAULT now();

ALTER TABLE documentos_auditoria 
    ALTER COLUMN data_envio SET DEFAULT now();