package br.com.boleiroOn.domain.arrematacao.dto;

import jakarta.validation.constraints.NotBlank;

public record AssinaturaArrematacaoRequestDto(

        @NotBlank(message = "A URL da foto da assinatura é obrigatória.")
        String fotoBase64,

        String assinaturaBase64
) {
}
