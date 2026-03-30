package br.com.boleiroOn.domain.user.dto;

import br.com.boleiroOn.domain.user.enums.UserRole;

public record UserUpdateDto(
            String name,
            String login,
            String oldPassword,
            String password,
            UserRole role
) {
}
