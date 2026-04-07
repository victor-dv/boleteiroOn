package br.com.boleiroOn.domain.arrematacao.dto;

import br.com.boleiroOn.domain.arrematacao.enums.StatusPagamentoArrematacao;

import java.math.BigDecimal;

public record ArrematacaoFeedDto(
        Long arrematacaoId,
        Integer numeroLote,
        String descricaoLote,
        boolean vendaOnline,
        String nomeArrematante,
        Integer placaArrematante,
        String fotoArrematante,
        BigDecimal valorArrematacao,
        StatusPagamentoArrematacao status,
        String urlFotoAssinatura

) {
}
