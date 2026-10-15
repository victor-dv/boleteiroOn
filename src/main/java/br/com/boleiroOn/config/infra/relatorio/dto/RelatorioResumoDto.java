package br.com.boleiroOn.config.infra.relatorio.dto;

import java.math.BigDecimal;

public record RelatorioResumoDto(
    Long leilaoId,
    String nomeLeilao,
    Long totalLotes,
    Long lotesArrematados,
    BigDecimal valorTotalArrematado,
    BigDecimal totalComissao,
    BigDecimal totalEstimado
) {}
