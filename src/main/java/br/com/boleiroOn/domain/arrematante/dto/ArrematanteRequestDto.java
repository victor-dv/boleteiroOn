package br.com.boleiroOn.domain.arrematante.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ArrematanteRequestDto(
        @NotNull
        Long leilaoId,

        @NotBlank
        String nome,
        @NotNull
        Integer placa,

        String telefone,

        String celular,


        @NotBlank
        @Email
        String email,

        String urlFotoDocumento,

        @NotBlank
        String documento,
        String rg,
        String cep,
        String cidade,
        String endereco,
        String uf,

        @Size(max = 1000)
        String observacao
) {
}
