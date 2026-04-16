package br.com.boleiroOn.config.infra.relatorio.controller;

import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioAuditoriaDto;
import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioFinanceiroDto;
import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioResumoDto;
import br.com.boleiroOn.config.infra.relatorio.service.RelatorioService;
import br.com.boleiroOn.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/resumo/{leilaoId}")
    public ResponseEntity<ApiResponse<RelatorioResumoDto>> getResumoLeilao(@PathVariable Long leilaoId) {
        var resumo = relatorioService.getResumoLeilao(leilaoId);
        return ResponseEntity.ok(ApiResponse.success(resumo, "Resumo do leilão obtido com sucesso"));
    }

    @GetMapping("/financeiro/{leilaoId}")
    public ResponseEntity<ApiResponse<List<RelatorioFinanceiroDto>>> getRelatorioFinanceiro(@PathVariable Long leilaoId) {
        var financeiro = relatorioService.getRelatorioFinanceiro(leilaoId);
        return ResponseEntity.ok(ApiResponse.success(financeiro, "Relatório financeiro obtido com sucesso"));
    }

    @GetMapping("/auditoria/{leilaoId}")
    public ResponseEntity<ApiResponse<List<RelatorioAuditoriaDto>>> getRelatorioAuditoria(@PathVariable Long leilaoId) {
        var auditoria = relatorioService.getRelatorioAuditoria(leilaoId);
        return ResponseEntity.ok(ApiResponse.success(auditoria, "Relatório de auditoria obtido com sucesso"));
    }

    @GetMapping("/excel/{leilaoId}")
    public ResponseEntity<byte[]> downloadRelatorioExcel(@PathVariable Long leilaoId) throws IOException {
        byte[] content = relatorioService.gerarRelatorioExcel(leilaoId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_arrematacao_" + leilaoId + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(content);
    }
}
