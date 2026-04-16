package br.com.boleiroOn.config.infra.relatorio.dto;

import java.math.BigDecimal;

public record RelatorioExcelDto(
        Integer numeroLote,
        String arrematanteNome,
        String arrematanteEmail,
        String arrematanteDocumento,
        String arrematanteCel,
        String arrematanteCep,
        String arrematanteEndereco,
        String arrematanteCidade,
        String arrematanteUf,
        BigDecimal valorArrematacao,
        BigDecimal valorComissao,
        BigDecimal totalPagar

        ) {
}
