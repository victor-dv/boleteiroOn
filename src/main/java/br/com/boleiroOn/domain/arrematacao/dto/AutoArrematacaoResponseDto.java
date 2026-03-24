package br.com.boleiroOn.domain.arrematacao.dto;

import br.com.boleiroOn.domain.arrematacao.entity.ArrematacaoEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AutoArrematacaoResponseDto(

        String leilaoNome,
        LocalDate leilaoData,

        Integer numeroLote,
        BigDecimal valorArrematacao,
        BigDecimal valorComissao,

        Integer placa,
        String name,
        String telefone,
        String celular,
        String email,
        String enderecoCompleto,

        String urlFotoAssinatura

) {

    public AutoArrematacaoResponseDto(ArrematacaoEntity entity) {
        this(
                entity.getLote().getLeilao().getNome(),
                entity.getLote().getLeilao().getData(),

                entity.getLote().getNumeroLote(),
                entity.getValorArrematacao(),
                entity.getValorComissao(),

                entity.getArrematante().getPlaca(),
                entity.getArrematante().getNome(),
                entity.getArrematante().getTelefone(),
                entity.getArrematante().getCelular(),
                entity.getArrematante().getEmail(),

                entity.getArrematante().getEndereco() + ", " +
                        entity.getArrematante().getCidade() + " - " +
                        entity.getArrematante().getUf(),

                entity.getUrlFotoAssinatura()
        );
    }
}
