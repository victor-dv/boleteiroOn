CREATE TABLE documentos_auditoria (
    id BIGSERIAL PRIMARY KEY,
    leilao_id BIGINT NOT NULL,
    arrematante_id BIGINT NOT NULL,
    lote_id BIGINT NOT NULL,
    tipo_documento VARCHAR(50) NOT NULL,
    url_s3 VARCHAR(500) NOT NULL,
    status_email VARCHAR(20) NOT NULL,
    data_envio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_documentos_auditoria_leilao FOREIGN KEY (leilao_id) REFERENCES leiloes(id),
    CONSTRAINT fk_documentos_auditoria_arrematante FOREIGN KEY (arrematante_id) REFERENCES arrematantes(id),
    CONSTRAINT fk_documentos_auditoria_lote FOREIGN KEY (lote_id) REFERENCES lotes(id)
);

CREATE INDEX idx_auditoria_leilao ON documentos_auditoria(leilao_id);
CREATE INDEX idx_auditoria_data ON documentos_auditoria(data_envio);
