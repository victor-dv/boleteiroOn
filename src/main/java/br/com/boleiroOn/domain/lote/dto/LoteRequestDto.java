package br.com.boleiroOn.domain.lote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LoteRequestDto (
        @NotNull
        Long leilaoId,

        @NotNull
        @Positive
        Integer numeroLote,

        @NotBlank
        String descricao,

        @NotNull
        Double valorInicial,

        @NotNull
        Double valorAvaliacao
) {
}
