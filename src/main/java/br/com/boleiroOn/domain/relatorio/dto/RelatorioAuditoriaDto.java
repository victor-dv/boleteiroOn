package br.com.boleiroOn.domain.relatorio.dto;

import java.time.LocalDateTime;

public record RelatorioAuditoriaDto(
    Long id,
    String arrematanteNome,
    String loteDescricao,
    String tipoDocumento,
    String statusEmail,
    LocalDateTime dataEnvio,
    String urlS3
) {}
