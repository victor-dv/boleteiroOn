package br.com.boleiroOn.domain.lote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record LoteRequestDto (
        @NotNull
        Long leilaoId,

        @NotNull
        @Positive
        Integer numeroLote,

        @NotBlank
        String descricao,

        @NotNull
        BigDecimal valorInicial,

        @NotNull
        BigDecimal valorAvaliacao
) {
}
