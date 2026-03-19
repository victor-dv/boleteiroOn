package br.com.boleiroOn.domain.leilao.dto;

import br.com.boleiroOn.domain.leilao.entity.LeilaoEntity;

import java.time.LocalDate;

public record LeilaoDetalhadoResponseDto(
        Long id,
        String nome,
        LocalDate data,
        boolean status,
        String imageUrl,
        String comitenteNome,
        String comitenteDocumento,
        String edital,
        String processoAdministrativo,
        String leiloeiroNome,
        String leiloeiroMatricula,
        String leiloeiroDocumento,
        String cidadeUfPagamento
) {
    public LeilaoDetalhadoResponseDto(LeilaoEntity entity) {
        this(entity.getId(), entity.getNome(), entity.getData(), entity.isStatus(),
                entity.getImageUrl(), entity.getComitenteNome(), entity.getComitenteDocumento(),
                entity.getEdital(), entity.getProcessoAdministrativo(), entity.getLeiloeiroNome(),
                entity.getLeiloeiroMatricula(), entity.getLeiloeiroDocumento(), entity.getCidadeUfPagamento());
    }
}
