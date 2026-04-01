package br.com.boleiroOn.domain.arrematacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ArrematacaoRequestEditValDto (
        @NotNull
        @Positive(message = "O valor da arrematação deve ser positivo.")
        BigDecimal valorArrematacao
){
}
