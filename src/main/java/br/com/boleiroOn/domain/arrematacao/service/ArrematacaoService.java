package br.com.boleiroOn.domain.arrematacao.service;

import br.com.boleiroOn.config.infra.aws.AWSService;
import br.com.boleiroOn.domain.arrematacao.dto.*;
import br.com.boleiroOn.domain.arrematacao.entity.ArrematacaoEntity;
import br.com.boleiroOn.domain.arrematacao.enums.StatusPagamentoArrematacao;
import br.com.boleiroOn.domain.arrematacao.repository.ArrematacaoRepository;
import br.com.boleiroOn.domain.arrematante.repository.ArrematanteRepository;
import br.com.boleiroOn.domain.lote.repository.LoteRepository;
import br.com.boleiroOn.shared.exception.BusinessException;
import br.com.boleiroOn.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArrematacaoService {
    private final ArrematacaoRepository arrematacaoRepository;
    private final LoteRepository loteRepository;
    private final ArrematanteRepository arrematanteRepository;
    private final PdfGeradorService pdfGeradorService;
    private final AWSService s3StorageService;

    @Transactional
    public ArrematacaoEntity create(ArrematacaoRequestDto data) {
        var lote = loteRepository.findByLeilaoIdAndNumeroLote(data.leilaoId(), data.numeroLote())
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado para o leilão e número de lote informados."));

        if (arrematacaoRepository.existsByLoteIdAndStatusNot(lote.getId(), StatusPagamentoArrematacao.CANCELADO)) {
            throw new BusinessException("Este lote já possui uma arrematação ativa.");
        }

        ArrematacaoEntity arrematacao = new ArrematacaoEntity();
        arrematacao.setLote(lote);
        arrematacao.setValorArrematacao(data.valorArrematacao());
        arrematacao.setVendaOnline(data.vendaOnline());
        arrematacao.setVendaCondicional(data.vendaCondicional());
        arrematacao.setStatus(StatusPagamentoArrematacao.PENDENTE_PAGAMENTO);

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
    public void assinarAutoArrematacao(Long arrematacaoId, AssinaturaArrematacaoRequestDto data) {
        var arrematacao = arrematacaoRepository.findById(arrematacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Arrematação não encontrada."));

        if (arrematacao.isVendaOnline()) {
            throw new BusinessException("Esta arrematação é de venda online e não pode ser assinada presencialmente.");
        }
        if (arrematacao.getUrlFotoAssinatura() != null) {
            throw new BusinessException("Esta arrematação já possui uma assinatura registrada.");
        }

        try {
            // 1. Extrair a String do DTO (Ex: "data:image/jpeg;base64,iVBORw0...")
            String fotoBase64Completa = data.fotoBase64();

            // 2. Separar o cabeçalho do conteúdo puro para salvar a foto
            String[] partesBase64 = fotoBase64Completa.split(",");
            String base64Puro = partesBase64.length > 1 ? partesBase64[1] : partesBase64[0];
            byte[] fotoBytes = Base64.getDecoder().decode(base64Puro);

            // 3. Gerar o PDF (Passando a String COMPLETA para o Thymeleaf embutir no HTML)
            byte[] pdfBytes = pdfGeradorService.gerarAutoArrematacaoPdf(arrematacao, fotoBase64Completa);

            // 4. Fazer upload da FOTO no S3
            String nomeArquivoFoto = "fotos-assinaturas/assinatura_arrematacao_" + arrematacaoId + "_" + System.currentTimeMillis() + ".jpg";
            String keyFotoS3 = s3StorageService.uploadFile(fotoBytes, nomeArquivoFoto, "image/jpeg");

            // 5. Fazer upload do PDF no S3
            String nomeArquivoPdf = "autos-arrematacao/auto_lote_" + arrematacao.getLote() + "_" + System.currentTimeMillis() + ".pdf";
            String keyPdfS3 = s3StorageService.uploadFile(pdfBytes, nomeArquivoPdf, "application/pdf");

            // 6. Atualizar a entidade com as chaves (URLs) geradas pelo S3
            arrematacao.setUrlFotoAssinatura(keyFotoS3);

            // Obs: Crie o campo e o método setUrlAutoPdf na sua entidade Arrematacao caso ainda não exista
            arrematacao.setUrlAutoPdf(keyPdfS3);

            // 7. Salvar no banco
            arrematacaoRepository.save(arrematacao);

        } catch (Exception e) {
            // Como a geração de PDF e o upload para o S3 podem lançar exceções (IOException, etc),
            // capture o erro genérico (ou os específicos) e relance como uma exceção de negócio
            // ou RuntimeException para que o @Transactional faça o rollback corretamente.
            throw new BusinessException("Erro ao processar a assinatura e gerar o auto de arrematação: " + e.getMessage());
        }
    }

    public List<ArrematacaoFeedDto> buscarFeedArrematacoes(Long leilaoId) {
        return arrematacaoRepository.buscarUltimasArrematacoesDoLeilao(leilaoId);
    }

    public List<ArrematacaoFeedDto> buscarAutoSemAssinatura(Long leilaoId) {
        var arrematacoes = arrematacaoRepository.buscarUltimasArrematacoesDoLeilao(leilaoId);
        return arrematacoes.stream()
                .filter(a -> a.urlFotoAssinatura() == null && !a.vendaOnline())
                .toList();
    }

    public List<ArrematacaoFeedDto> buscatrAutoAssinada(Long leilaoId) {
        var arrematacoes = arrematacaoRepository.buscarUltimasArrematacoesDoLeilao(leilaoId);
        return arrematacoes.stream()
                .filter(a -> a.urlFotoAssinatura() != null && !a.vendaOnline())
                .toList();
    }

    public ArrematacaoEntity editarValorArrematacao(Long arrematacaoId, ArrematacaoRequestEditValDto data) {
        var arrematacao = arrematacaoRepository.findById(arrematacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Arrematação não encontrada."));
        arrematacao.setValorArrematacao(data.valorArrematacao());

        if (arrematacao.getValorArrematacao().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor da arrematação deve ser positivo.");
        }

        return arrematacaoRepository.save(arrematacao);
    }


}
