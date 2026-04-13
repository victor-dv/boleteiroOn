package br.com.boleiroOn.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DefinePasswordDto(
        @NotBlank
        String token,
        @NotBlank
        @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres")
        String newPassword) {

}
