package br.com.boleiroOn.domain.lote.service;

import br.com.boleiroOn.domain.leilao.repository.LeilaoRepository;
import br.com.boleiroOn.domain.lote.dto.LoteRequestDto;
import br.com.boleiroOn.domain.lote.entity.LoteEntity;
import br.com.boleiroOn.domain.lote.repository.LoteRepository;
import br.com.boleiroOn.shared.exception.BusinessException;
import br.com.boleiroOn.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoteService {
    private final LoteRepository loteRepository;
    private final LeilaoRepository leilaoRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public LoteEntity create(LoteRequestDto data) {
        var leilao = leilaoRepository.findById(data.leilaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado."));

        if (loteRepository.existsByLeilaoIdAndNumeroLote(data.leilaoId(), data.numeroLote())) {
            throw new BusinessException("Já existe um lote com este número neste leilão.");
        }

        LoteEntity lote = new LoteEntity();
        lote.setLeilao(leilao);
        lote.setNumeroLote(data.numeroLote());
        lote.setDescricao(data.descricao());

        return loteRepository.save(lote);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public List<LoteEntity> importarDeExcel(Long leilaoId, MultipartFile file) {
        var leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado."));

        List<LoteEntity> lotesParaSalvar = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                if (row.getCell(0) == null || row.getCell(1) == null) continue;

                int numeroLote = (int) row.getCell(0).getNumericCellValue();
                String descricao = row.getCell(1).getStringCellValue();

                if (loteRepository.existsByLeilaoIdAndNumeroLote(leilaoId, numeroLote)) {
                    continue;
                }

                LoteEntity lote = new LoteEntity();
                lote.setLeilao(leilao);
                lote.setNumeroLote(numeroLote);
                lote.setDescricao(descricao);

                lotesParaSalvar.add(lote);
            }

            return loteRepository.saveAll(lotesParaSalvar);

        } catch (Exception e) {
            throw new BusinessException("Erro ao processar o arquivo Excel: " + e.getMessage());
        }
    }

    public List<LoteEntity> getAll(Long leilaoId) {
        return loteRepository.findAll();
    }

}
