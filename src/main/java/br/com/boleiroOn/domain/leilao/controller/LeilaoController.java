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
    @PutMapping("/{id}")
    public ResponseEntity <ApiResponse<LeilaoResponseDto>> update(@PathVariable Long id, @RequestBody @Valid LeilaoRequestDto dto) {
        var leilaoAtualizado = leilaoService.update(id, dto);
        var response = new LeilaoResponseDto(leilaoAtualizado);
        return ResponseEntity.ok(ApiResponse.success(response, "Leilão atualizado com sucesso"));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<LeilaoDetalhadoResponseDto>>> getAll() {
        List<LeilaoDetalhadoResponseDto> leiloes = leilaoService.getAll().stream().map(LeilaoDetalhadoResponseDto::new).toList();
        return ResponseEntity.ok(ApiResponse.success(leiloes, "Leilões obtidos com sucesso"));}

    @GetMapping("/{status}")
    public ResponseEntity<ApiResponse<List<LeilaoDetalhadoResponseDto>>> getByStatus(@PathVariable boolean status) {
        List<LeilaoDetalhadoResponseDto> leiloes = leilaoService.getByStatus(status).stream().map(LeilaoDetalhadoResponseDto::new).toList();
        return ResponseEntity.ok(ApiResponse.success(leiloes, "Leilões obtidos com sucesso"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<LeilaoResponseDto>> arquivarLeilao(@PathVariable Long id) {
        var leilaoAtualizado = leilaoService.falseDelete(id);
        var responseDto = new LeilaoResponseDto(leilaoAtualizado);
        return ResponseEntity.ok(ApiResponse.success(responseDto, "Leilão atualizado com sucesso"));
    }
    @PatchMapping("/{id}/voltar")
    public ResponseEntity<ApiResponse<LeilaoResponseDto>> voltarLeilao(@PathVariable Long id) {
        var leilaoAtualizado = leilaoService.voltaLeilao(id);
        var responseDto = new LeilaoResponseDto(leilaoAtualizado);
        return ResponseEntity.ok(ApiResponse.success(responseDto, "Leilão atualizado com sucesso"));
    }
}