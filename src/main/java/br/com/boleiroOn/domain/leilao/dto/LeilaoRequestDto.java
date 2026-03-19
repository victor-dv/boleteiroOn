package br.com.boleiroOn.domain.leilao.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LeilaoRequestDto(
        @NotBlank
        String nome,

        @NotNull
        @FutureOrPresent
        LocalDate data,

        String imageUrl,

        @NotBlank
        String comitenteNome,
        @NotBlank
        String comitenteDocumento,
        @NotBlank
        String edital,
        String processoAdministrativo,
        @NotBlank
        String leiloeiroNome,
        @NotBlank
        String leiloeiroMatricula,
        @NotBlank
        String leiloeiroDocumento,
        @NotBlank
        String cidadeUfPagamento
) {
}