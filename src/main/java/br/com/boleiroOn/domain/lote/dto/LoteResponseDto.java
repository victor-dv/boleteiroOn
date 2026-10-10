package br.com.boleiroOn.domain.lote.dto;

import br.com.boleiroOn.domain.lote.entity.LoteEntity;

import java.math.BigDecimal;

public record LoteResponseDto(
        Long id,
        Long leilaoId,
        Integer numeroLote,
        String descricao,
        BigDecimal valorInicial,
        BigDecimal valorAvaliacao
) {
    public LoteResponseDto(LoteEntity entity) {
        this(
                entity.getId(),
                entity.getLeilao().getId(),
                entity.getNumeroLote(),
                entity.getDescricao(),
                entity.getValorInicial(),
                entity.getValorAvaliacao()
        );
    }
}
