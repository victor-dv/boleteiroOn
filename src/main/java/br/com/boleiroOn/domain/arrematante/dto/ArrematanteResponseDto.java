package br.com.boleiroOn.domain.arrematante.dto;

import br.com.boleiroOn.domain.arrematante.entity.ArrematanteEntity;

public record ArrematanteResponseDto(
        Long id,
        Long leilaoId,
        String nome,
        Integer placa,
        String email,
        String urlFotoDocumento
) {
    public ArrematanteResponseDto(ArrematanteEntity entity) {
        this(
                entity.getId(),
                entity.getLeilao().getId(),
                entity.getNome(),
                entity.getPlaca(),
                entity.getEmail(),
                entity.getUrlFotoDocumento()
        );
    }
}
