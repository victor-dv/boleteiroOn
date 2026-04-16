package br.com.boleiroOn.config.infra.relatorio.service;

import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioAuditoriaDto;
import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioExcelDto;
import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioFinanceiroDto;
import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioResumoDto;
import br.com.boleiroOn.config.infra.relatorio.repository.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;

    @Transactional(readOnly = true)
    public RelatorioResumoDto getResumoLeilao(Long leilaoId) {
        return relatorioRepository.buscarResumoLeilao(leilaoId);
    }

    @Transactional(readOnly = true)
    public List<RelatorioFinanceiroDto> getRelatorioFinanceiro(Long leilaoId) {
        return relatorioRepository.buscarRelatorioFinanceiro(leilaoId);
    }

    @Transactional(readOnly = true)
    public List<RelatorioAuditoriaDto> getRelatorioAuditoria(Long leilaoId) {
        return relatorioRepository.buscarRelatorioAuditoria(leilaoId);
    }

    @Transactional(readOnly = true)
    public byte[] gerarRelatorioExcel(Long leilaoId) throws IOException {
        List<RelatorioExcelDto> dados = relatorioRepository.buscarDadosParaExcel(leilaoId);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Relatório de Arrematação");

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            Row headerRow = sheet.createRow(0);
            String[] columns = {"Lote", "Arrematante", "Email", "CPF", "Celular", "Valor Arrematação", "Comissão", "Total a Pagar", "Cep","Endereço", "Cidade", "UF"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (RelatorioExcelDto dto : dados) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.numeroLote() != null ? dto.numeroLote() : 0);
                row.createCell(1).setCellValue(dto.arrematanteNome());
                row.createCell(2).setCellValue(dto.arrematanteEmail());
                row.createCell(3).setCellValue(dto.arrematanteDocumento());
                row.createCell(4).setCellValue(dto.arrematanteCel());
                row.createCell(5).setCellValue(dto.valorArrematacao() != null ? dto.valorArrematacao().doubleValue() : 0.0);
                row.createCell(6).setCellValue(dto.valorComissao() != null ? dto.valorComissao().doubleValue() : 0.0);
                row.createCell(7).setCellValue(dto.totalPagar() != null ? dto.totalPagar().doubleValue() : 0.0);
                row.createCell(8).setCellValue(dto.arrematanteCep());
                row.createCell(9).setCellValue(dto.arrematanteEndereco());
                row.createCell(10).setCellValue(dto.arrematanteCidade());
                row.createCell(11).setCellValue(dto.arrematanteUf());

            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
