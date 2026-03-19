package br.com.boleiroOn.domain.arrematacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ArrematacaoRequestDto (
        @NotNull
        Long loteId,
        Integer placa,
        @NotNull
        Boolean vendaOnline,
        @NotNull
        @Positive
        BigDecimal valorArrematacao
){
}
