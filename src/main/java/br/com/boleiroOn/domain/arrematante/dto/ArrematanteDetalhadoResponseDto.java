package br.com.boleiroOn.domain.arrematante.dto;

import br.com.boleiroOn.domain.arrematante.entity.ArrematanteEntity;
import br.com.boleiroOn.domain.leilao.dto.LeilaoDetalhadoResponseDto;

public record ArrematanteDetalhadoResponseDto(
        Long id,
        String nome,
        Integer placa,
        String email,
        String urlFotoDocumento,
        String documento,
        String telefone,
        String celular,
        String rg,
        String endereco,
        String cep,
        String cidade,
        String uf,
        String observacao,

        LeilaoDetalhadoResponseDto leilao
) {
    public ArrematanteDetalhadoResponseDto(ArrematanteEntity entity) {
        this(
                entity.getId(), entity.getNome(),
                entity.getPlaca(), entity.getEmail(), entity.getUrlFotoDocumento(),
                entity.getDocumento(),entity.getTelefone(), entity.getCelular(), entity.getRg(), entity.getEndereco(),
                entity.getCep(), entity.getCidade(), entity.getUf(),
                entity.getObservacao(),

                new LeilaoDetalhadoResponseDto(entity.getLeilao())
        );
    }
}