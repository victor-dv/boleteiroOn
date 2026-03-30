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

    @GetMapping("/feed/{leilaoId}")
    public ResponseEntity<ApiResponse<List<ArrematacaoFeedDto>>> buscarFeed(@PathVariable Long leilaoId) {
        List<ArrematacaoFeedDto> feed = arrematacaoService.buscarFeedArrematacoes(leilaoId);
        return ResponseEntity.ok(ApiResponse.success(feed, "Feed de arrematações atualizado."));
    }


}
