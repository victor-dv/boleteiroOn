package br.com.boleiroOn.domain.user.dto;

public record LoginResponseDto(
        String token,
        String name,
        String email
) {
}
