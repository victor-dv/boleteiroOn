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
        boolean vendaCondicional,
        BigDecimal valorArrematacao,
        BigDecimal valorComissao
) {
        // Método que calcula a soma em tempo real no Java
        public BigDecimal getTotalPagar() {
                BigDecimal arrematacao = valorArrematacao != null ? valorArrematacao : BigDecimal.ZERO;
                BigDecimal comissao = valorComissao != null ? valorComissao : BigDecimal.ZERO;
                return arrematacao.add(comissao);
        }
}