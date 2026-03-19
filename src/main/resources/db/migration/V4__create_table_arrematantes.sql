CREATE TABLE arrematantes (
    id BIGSERIAL PRIMARY KEY,
    leilao_id BIGINT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    modalidade VARCHAR(50) NOT NULL,
    placa INTEGER,
    telefone VARCHAR(20),
    celular VARCHAR(20),
    endereco VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    url_foto_documento VARCHAR(500),
    CONSTRAINT fk_arrematante_leilao FOREIGN KEY (leilao_id) REFERENCES leiloes(id)
);