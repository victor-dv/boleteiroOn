ALTER TABLE leiloes
ADD COLUMN comitente_nome VARCHAR(255),
ADD COLUMN comitente_documento VARCHAR(50),
ADD COLUMN edital VARCHAR(50),
ADD COLUMN processo_administrativo VARCHAR(50),
ADD COLUMN leiloeiro_nome VARCHAR(255),
ADD COLUMN leiloeiro_matricula VARCHAR(50),
ADD COLUMN leiloeiro_documento VARCHAR(50),
ADD COLUMN cidade_uf_pagamento VARCHAR(100);

ALTER TABLE arrematantes
ADD COLUMN documento VARCHAR(50),
ADD COLUMN rg VARCHAR(50),
ADD COLUMN cep VARCHAR(20),
ADD COLUMN cidade VARCHAR(100),
ADD COLUMN uf VARCHAR(2);