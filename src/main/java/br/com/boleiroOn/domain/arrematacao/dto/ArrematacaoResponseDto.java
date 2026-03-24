package br.com.boleiroOn.domain.arrematacao.dto;

import br.com.boleiroOn.domain.arrematacao.entity.ArrematacaoEntity;
import br.com.boleiroOn.domain.arrematacao.enums.StatusArrematacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ArrematacaoResponseDto(
        Long id,
        Long loteId,
        Integer numeroLote,
        Long arrematanteId,
        String nomeArrematante,
        Integer placaArrematante,
        boolean vendaOnline,
        BigDecimal valorArrematacao,
        BigDecimal valorComissao,
        StatusArrematacao status,
        LocalDateTime dataArrematacao
) {
    public ArrematacaoResponseDto(ArrematacaoEntity entity) {
        this(
                entity.getId(),
                entity.getLote().getId(),
                entity.getLote().getNumeroLote(),

                entity.isVendaOnline() ? null : entity.getArrematante().getId(),
                entity.isVendaOnline() ? "Arrematante WEB" : entity.getArrematante().getNome(),
                entity.isVendaOnline() ? null : entity.getArrematante().getPlaca(),

                entity.isVendaOnline(),
                entity.getValorArrematacao(),
                entity.getValorComissao(),
                entity.getStatus(),
                entity.getDataArrematacao()
        );
    }
}
