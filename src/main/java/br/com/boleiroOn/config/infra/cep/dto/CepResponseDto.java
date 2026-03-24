package br.com.boleiroOn.config.infra.cep.dto;

public record CepResponseDto(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf
) {

}
