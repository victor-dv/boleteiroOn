package br.com.boleiroOn.domain.arrematacao.controller;

import br.com.boleiroOn.domain.arrematacao.dto.*;
import br.com.boleiroOn.domain.arrematacao.service.ArrematacaoService;
import br.com.boleiroOn.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/arrematacao")
@RequiredArgsConstructor
public class ArrematacaoController {
    private final ArrematacaoService arrematacaoService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<ArrematacaoResponseDto>> create(@RequestBody @Valid ArrematacaoRequestDto data, UriComponentsBuilder uriBuilder){
        var novaArrematacao = arrematacaoService.create(data);
        var responseDto = new ArrematacaoResponseDto(novaArrematacao);
        var uri = uriBuilder.path("/arrematacoes/{id}").buildAndExpand(novaArrematacao.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(ApiResponse.success(responseDto, "Arrematação registrada com sucesso."));
    }

    @GetMapping("/{id}/auto")
    public ResponseEntity<ApiResponse<AutoArrematacaoResponseDto>>buscarAuto(@PathVariable Long id){
        var auto = arrematacaoService.buscarAutoArrematacao(id);
        return ResponseEntity.ok(ApiResponse.success(auto, "Dados do arrematante presencial obtidos com sucesso."));
    }

    @PatchMapping("/{id}/assinatura")
    public ResponseEntity<ApiResponse<Void>>assinar(@PathVariable Long id, @RequestBody @Valid AssinaturaArrematacaoRequestDto data) {
        arrematacaoService.assinarAutoArrematacao(id, data);
        return ResponseEntity.ok(ApiResponse.success(null, "Arrematação assinada com sucesso."));
    }
    @PatchMapping("/{id}/valor")
    public ResponseEntity<ApiResponse<Void>>editarValor(@PathVariable Long id, @RequestBody @Valid ArrematacaoRequestEditValDto data) {
        arrematacaoService.editarValorArrematacao(id, data);
        return ResponseEntity.ok(ApiResponse.success(null, "Valor da arrematação atualizado com sucesso."));
    }

    @PatchMapping("/{id}/placa")
    public ResponseEntity<ApiResponse<Void>>mudarPlaca(@PathVariable Long id, @RequestBody @Valid ArrematacaoUpdatePlacaDto data) {
        arrematacaoService.mudarPlacaArrematante(id, data);
        return ResponseEntity.ok(ApiResponse.success(null, "Placa do arrematante atualizada com sucesso."));
    }

    @GetMapping("/feed/{leilaoId}")
    public ResponseEntity<ApiResponse<List<ArrematacaoFeedDto>>> buscarFeed(@PathVariable Long leilaoId) {
        List<ArrematacaoFeedDto> feed = arrematacaoService.buscarFeedArrematacoes(leilaoId);
        return ResponseEntity.ok(ApiResponse.success(feed, "Feed de arrematações atualizado."));
    }

    @GetMapping("feed/autos-nulos/{leilaoId}")
    public ResponseEntity<ApiResponse<List<ArrematacaoFeedDto>>> buscarFeedAutosNulos(@PathVariable Long leilaoId) {
        List<ArrematacaoFeedDto> feed = arrematacaoService.buscarAutoSemAssinatura(leilaoId);
        return ResponseEntity.ok(ApiResponse.success(feed, "Feed de arrematações com autos nulos atualizado."));
    }

    @GetMapping("feed/autos-assinados/{leilaoId}")
    public ResponseEntity<ApiResponse<List<ArrematacaoFeedDto>>> buscarFeedAutosAssinados(@PathVariable Long leilaoId) {
        List<ArrematacaoFeedDto> feed = arrematacaoService.buscatrAutoAssinada(leilaoId);
        return ResponseEntity.ok(ApiResponse.success(feed, "Feed de arrematações com autos assinados atualizado."));
    }

}
