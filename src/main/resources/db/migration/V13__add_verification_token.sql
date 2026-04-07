ALTER TABLE arrematantes ADD COLUMN email_validado BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE tokens_validacao_arrematante (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    arrematante_id BIGINT NOT NULL,
    data_expiracao TIMESTAMP NOT NULL,
    CONSTRAINT fk_token_arrematante FOREIGN KEY (arrematante_id) REFERENCES arrematantes (id) ON DELETE CASCADE
);