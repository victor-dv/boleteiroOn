package br.com.boleiroOn.domain.leilao.service;

import br.com.boleiroOn.domain.leilao.dto.LeilaoRequestDto;
import br.com.boleiroOn.domain.leilao.dto.LeilaoResponseDto;
import br.com.boleiroOn.domain.leilao.entity.LeilaoEntity;
import br.com.boleiroOn.domain.leilao.repository.LeilaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeilaoService {

    private final LeilaoRepository leilaoRepository;
    @Transactional
    public LeilaoEntity create(LeilaoRequestDto data) {
        LeilaoEntity leilao = new LeilaoEntity();
        leilao.setNome(data.nome());
        leilao.setData(data.data());
        leilao.setImageUrl(data.imageUrl());

        leilao.setComitenteNome(data.comitenteNome());
        leilao.setComitenteDocumento(data.comitenteDocumento());
        leilao.setEdital(data.edital());
        leilao.setProcessoAdministrativo(data.processoAdministrativo());
        leilao.setLeiloeiroNome(data.leiloeiroNome());
        leilao.setLeiloeiroMatricula(data.leiloeiroMatricula());
        leilao.setLeiloeiroDocumento(data.leiloeiroDocumento());
        leilao.setCidadeUfPagamento(data.cidadeUfPagamento());

        return leilaoRepository.save(leilao);
    }

    public List<LeilaoEntity> getAll() {
        return leilaoRepository.findAll();
    }

    public LeilaoEntity falseDelete(Long id) {
        var leilao = leilaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leilão não encontrado."));
        leilao.setStatus(false);
        return leilaoRepository.save(leilao);
    }

    public LeilaoEntity voltaLeilao(Long id) {
        var leilao = leilaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leilão não encontrado."));
        leilao.setStatus(true);
        return leilaoRepository.save(leilao);
    }

    public List<LeilaoEntity> getByStatus(boolean status) {
        return leilaoRepository.findByStatus(status);
    }

}
