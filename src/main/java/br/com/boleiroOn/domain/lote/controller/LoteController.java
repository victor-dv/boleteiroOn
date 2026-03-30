package br.com.boleiroOn.domain.lote.controller;

import br.com.boleiroOn.domain.lote.dto.LoteRequestDto;
import br.com.boleiroOn.domain.lote.dto.LoteResponseDto;
import br.com.boleiroOn.domain.lote.service.LoteService;
import br.com.boleiroOn.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/lotes")
@RequiredArgsConstructor
public class LoteController {
    private final LoteService loteService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<LoteResponseDto>> create(@RequestBody @Valid LoteRequestDto data, UriComponentsBuilder uriBuilder) {

        var novoLote = loteService.create(data);
        var responseDto = new LoteResponseDto(novoLote);
        var uri = uriBuilder.path("/lotes/{id}").buildAndExpand(novoLote.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(ApiResponse.success(responseDto, "Lote criado com sucesso"));
    }
    @PostMapping("/leilao/{leilaoId}/importar")
    public ResponseEntity<ApiResponse<List<LoteResponseDto>>> importarLotes(@PathVariable Long leilaoId, @RequestParam("file") MultipartFile file) {

        var lotesImportados = loteService.importarDeExcel(leilaoId, file);

        var responseDtoList = lotesImportados.stream()
                .map(LoteResponseDto::new)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(responseDtoList,
                lotesImportados.size() + " lotes importados com sucesso."));
    }
    @GetMapping("/leilao/{leilaoId}")
    public ResponseEntity<ApiResponse<List<LoteResponseDto>>> listAll(@PathVariable Long leilaoId) {
        var lotes = loteService.getAll(leilaoId);
        var responseDtoList = lotes.stream()
                .map(LoteResponseDto::new)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(responseDtoList, "Lotes do leilão obtidos com sucesso"));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<LoteResponseDto>> delete(@PathVariable Long id) {
        var lote = loteService.delete(id);
        var responseDto = new LoteResponseDto(lote);

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Lote deletado com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LoteResponseDto>> update(@PathVariable Long id, @RequestBody @Valid LoteRequestDto data) {
        var lote = loteService.update(id, data);
        var responseDto = new LoteResponseDto(lote);

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Lote atualizado com sucesso"));
    }

}
