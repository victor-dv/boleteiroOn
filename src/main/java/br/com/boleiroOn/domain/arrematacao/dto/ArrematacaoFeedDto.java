package br.com.boleiroOn.domain.arrematacao.dto;

import br.com.boleiroOn.domain.arrematacao.enums.StatusArrematacao;

import java.math.BigDecimal;

public record ArrematacaoFeedDto(
        Long arrematacaoId,
        Integer numeroLote,
        String descricaoLote,
        String nomeArrematante,
        String fotoArrematante,
        BigDecimal valorArrematacao,
        StatusArrematacao status
) {
}
