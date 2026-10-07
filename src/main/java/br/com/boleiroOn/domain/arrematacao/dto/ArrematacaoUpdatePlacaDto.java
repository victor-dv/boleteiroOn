package br.com.boleiroOn.domain.arrematacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ArrematacaoUpdatePlacaDto (
        @NotNull(message = "A nova placa é obrigatória.")
        @Positive(message = "A placa deve ser um número positivo.")
        Integer novaPlaca
){
}