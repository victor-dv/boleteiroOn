package br.com.boleiroOn.domain.user.dto;

import br.com.boleiroOn.domain.user.entity.UserEntity;
import br.com.boleiroOn.domain.user.enums.UserRole;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        String login,
        UserRole role
) {
        public static UserResponseDto from(UserEntity entity) {
            return new UserResponseDto(
                    entity.getId(),
                    entity.getName(),
                    entity.getEmail(),
                    entity.getLogin(),
                    entity.getRole()
            );
        }
}
