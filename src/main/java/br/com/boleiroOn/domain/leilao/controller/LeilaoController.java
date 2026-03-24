package br.com.boleiroOn.domain.leilao.controller;

import br.com.boleiroOn.domain.leilao.dto.LeilaoDetalhadoResponseDto;
import br.com.boleiroOn.domain.leilao.dto.LeilaoRequestDto;
import br.com.boleiroOn.domain.leilao.dto.LeilaoResponseDto;
import br.com.boleiroOn.domain.leilao.service.LeilaoService;
import br.com.boleiroOn.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/leiloes")
@RequiredArgsConstructor
public class LeilaoController {

    private final LeilaoService leilaoService;


    @PostMapping("/")
    public ResponseEntity<ApiResponse<LeilaoResponseDto>> create(@RequestBody @Valid LeilaoRequestDto data, UriComponentsBuilder uriBuilder) {

        var novoLeilao = leilaoService.create(data);
        var responseDto = new LeilaoResponseDto(novoLeilao);
        var uri = uriBuilder.path("/leiloes/{id}").buildAndExpand(novoLeilao.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(ApiResponse.success(responseDto, "Leilão criado com sucesso"));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<LeilaoDetalhadoResponseDto>>> getAll() {
        List<LeilaoDetalhadoResponseDto> leiloes = leilaoService.getAll().stream().map(LeilaoDetalhadoResponseDto::new).toList();
        return ResponseEntity.ok(ApiResponse.success(leiloes, "Leilões obtidos com sucesso"));}
}