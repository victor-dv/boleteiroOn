package br.com.boleiroOn.domain.lote.dto;

import br.com.boleiroOn.domain.lote.entity.LoteEntity;

public record LoteResponseDto(
        Long id,
        Long leilaoId,
        Integer numeroLote,
        String descricao,
        Double valorInicial,
        Double valorAvaliacao
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
