CREATE TABLE arrematacoes (
    id BIGSERIAL PRIMARY KEY,
    lote_id BIGINT NOT NULL,
    arrematante_id BIGINT NOT NULL,
    valor_arrematacao DECIMAL(15, 2) NOT NULL,
    valor_comissao DECIMAL(15, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_arrematacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_arrematacao_lote FOREIGN KEY (lote_id) REFERENCES lotes(id),
    CONSTRAINT fk_arrematacao_arrematante FOREIGN KEY (arrematante_id) REFERENCES arrematantes(id)
);

ALTER TABLE arrematantes
 DROP COLUMN modalidade;
