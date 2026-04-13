package br.com.boleiroOn.domain.user.dto;

import br.com.boleiroOn.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterDto(
        @NotBlank
        String name,
        @NotBlank @Email
        String email,
        @NotBlank
        String login,
        UserRole role
) {
}
