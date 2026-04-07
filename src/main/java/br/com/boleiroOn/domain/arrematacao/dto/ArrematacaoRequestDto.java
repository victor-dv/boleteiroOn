package br.com.boleiroOn.domain.arrematacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ArrematacaoRequestDto (
        @NotNull(message = "O ID do leilão é obrigatório.")
        Long leilaoId,
        @NotNull(message = "O número do lote é obrigatório.")
        @Positive
        Integer numeroLote,

        Integer placa,
        @NotNull
        Boolean vendaOnline,
        @NotNull
        Boolean vendaCondicional,
        @NotNull
        @Positive
        BigDecimal valorArrematacao
){
}
