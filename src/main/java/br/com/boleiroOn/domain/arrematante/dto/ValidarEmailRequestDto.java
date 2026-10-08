package br.com.boleiroOn.domain.arrematante.dto;

import jakarta.validation.constraints.NotBlank;

public record ValidarEmailRequestDto(
        @NotBlank
        String token
) {
}