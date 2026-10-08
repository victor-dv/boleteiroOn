package br.com.boleiroOn.config.infra.relatorio.dto;

import java.time.OffsetDateTime;

public record RelatorioAuditoriaDto(
    Long id,
    String arrematanteNome,
    String loteDescricao,
    Integer numeroLote,
    String tipoDocumento,
    String statusEmail,
    OffsetDateTime dataEnvio,
    String urlS3
) {}
