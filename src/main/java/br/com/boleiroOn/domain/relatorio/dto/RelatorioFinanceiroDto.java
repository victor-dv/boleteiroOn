package br.com.boleiroOn.domain.relatorio.dto;

import br.com.boleiroOn.domain.arrematacao.enums.StatusPagamentoArrematacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RelatorioFinanceiroDto(
    Long arrematacaoId,
    String arrematanteNome,
    String loteDescricao,
    BigDecimal valorArrematacao,
    BigDecimal valorComissao,
    StatusPagamentoArrematacao status,
    LocalDateTime dataArrematacao
) {}
