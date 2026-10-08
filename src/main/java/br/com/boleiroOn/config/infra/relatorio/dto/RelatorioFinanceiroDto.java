package br.com.boleiroOn.config.infra.relatorio.dto;

import br.com.boleiroOn.domain.arrematacao.enums.StatusPagamentoArrematacao;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record RelatorioFinanceiroDto(
    Long arrematacaoId,
    String arrematanteNome,
    String loteDescricao,
    Integer numeroLote,
    BigDecimal valorArrematacao,
    BigDecimal valorComissao,
    StatusPagamentoArrematacao status,
    OffsetDateTime dataArrematacao
) {}
