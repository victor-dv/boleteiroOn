package br.com.boleiroOn.domain.relatorio.dto;

import java.math.BigDecimal;

public record RelatorioResumoDto(
    Long leilaoId,
    String nomeLeilao,
    Long totalLotes,
    Long lotesArrematados,
    BigDecimal valorTotalArrematado,
    BigDecimal totalComissao
) {}
