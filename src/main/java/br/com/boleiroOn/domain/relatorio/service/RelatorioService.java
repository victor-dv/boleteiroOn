package br.com.boleiroOn.domain.relatorio.service;

import br.com.boleiroOn.domain.relatorio.dto.RelatorioAuditoriaDto;
import br.com.boleiroOn.domain.relatorio.dto.RelatorioFinanceiroDto;
import br.com.boleiroOn.domain.relatorio.dto.RelatorioResumoDto;
import br.com.boleiroOn.domain.relatorio.repository.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
