package br.com.boleiroOn.domain.arrematacao.service;

import br.com.boleiroOn.domain.arrematacao.dto.ArrematacaoRequestDto;
import br.com.boleiroOn.domain.arrematacao.dto.AssinaturaArrematacaoRequestDto;
import br.com.boleiroOn.domain.arrematacao.dto.AutoArrematacaoResponseDto;
import br.com.boleiroOn.domain.arrematacao.entity.ArrematacaoEntity;
import br.com.boleiroOn.domain.arrematacao.enums.StatusArrematacao;
import br.com.boleiroOn.domain.arrematacao.repository.ArrematacaoRepository;
import br.com.boleiroOn.domain.arrematante.repository.ArrematanteRepository;
import br.com.boleiroOn.domain.lote.repository.LoteRepository;
import br.com.boleiroOn.shared.exception.BusinessException;
import br.com.boleiroOn.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArrematacaoService {
    private final ArrematacaoRepository arrematacaoRepository;
    private final LoteRepository loteRepository;
    private final ArrematanteRepository arrematanteRepository;

    @Transactional
    public ArrematacaoEntity create(ArrematacaoRequestDto data) {
        var lote = loteRepository.findById(data.loteId())
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado."));

        if (arrematacaoRepository.existsByLoteIdAndStatusNot(lote.getId(), StatusArrematacao.CANCELADO)) {
            throw new BusinessException("Este lote já possui uma arrematação ativa.");
        }

        ArrematacaoEntity arrematacao = new ArrematacaoEntity();
        arrematacao.setLote(lote);
        arrematacao.setValorArrematacao(data.valorArrematacao());
        arrematacao.setVendaOnline(data.vendaOnline());
        arrematacao.setStatus(StatusArrematacao.PENDENTE_PAGAMENTO);

        if (data.vendaOnline()) {
            arrematacao.setArrematante(null);
        } else {
            if (data.placa() == null) {
                throw new BusinessException("Para vendas presenciais, a placa do arrematante é obrigatória.");
            }
            var arrematante = arrematanteRepository.findByLeilaoIdAndPlaca(lote.getLeilao().getId(), data.placa())
                    .orElseThrow(() -> new ResourceNotFoundException("Nenhum arrematante presencial encontrado com a placa " + data.placa() + " neste leilão."));

            arrematacao.setArrematante(arrematante);
        }

        return arrematacaoRepository.save(arrematacao);
    }

    @Transactional(readOnly = true)
    public AutoArrematacaoResponseDto buscarAutoArrematacao(Long arrematacaoId) {
        var arrematacao = arrematacaoRepository.findById(arrematacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Arrematação não encontrada."));
        if (arrematacao.isVendaOnline()){
            throw new BusinessException("Esta arrematação é de venda online e não possui dados de arrematante presencial.");
        }
        return new AutoArrematacaoResponseDto(arrematacao);
    }


    @Transactional
    public void assinarAutoArrematacao(Long arrematacaoId, AssinaturaArrematacaoRequestDto data){
        var arrematacao = arrematacaoRepository.findById(arrematacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Arrematação não encontrada."));

        if (arrematacao.isVendaOnline()){
            throw new BusinessException("Esta arrematação é de venda online e não pode ser assinada presencialmente.");
        }
        if (arrematacao.getUrlFotoAssinatura() != null){
            throw new BusinessException("Esta arrematação já possui uma assinatura registrada.");
        }
        arrematacao.setUrlFotoAssinatura(data.urlFotoAssinatura());

        arrematacaoRepository.save(arrematacao);
    }
}
