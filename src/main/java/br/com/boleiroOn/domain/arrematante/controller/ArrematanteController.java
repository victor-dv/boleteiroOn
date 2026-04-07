package br.com.boleiroOn.domain.arrematante.controller;

import br.com.boleiroOn.config.infra.email.service.EnviarEmailVerificationService;
import br.com.boleiroOn.domain.arrematante.dto.ArrematanteDetalhadoResponseDto;
import br.com.boleiroOn.domain.arrematante.dto.ArrematanteRequestDto;
import br.com.boleiroOn.domain.arrematante.dto.ArrematanteResponseDto;
import br.com.boleiroOn.domain.arrematante.service.ArrematanteService;
import br.com.boleiroOn.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/arrematantes")
@RequiredArgsConstructor
public class ArrematanteController {

    private final ArrematanteService arrematanteService;
    @PostMapping
    public ResponseEntity<ApiResponse<ArrematanteResponseDto>> create(@RequestBody @Valid ArrematanteRequestDto data, UriComponentsBuilder uriBuilder) {

        var novoArrematante = arrematanteService.create(data);
        var responseDto = new ArrematanteResponseDto(novoArrematante);
        var uri = uriBuilder.path("/arrematantes/{id}").buildAndExpand(novoArrematante.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(ApiResponse.success(responseDto, "Arrematante registrado com sucesso"));
    }

    @GetMapping("/leilao/{leilaoId}")
    public ResponseEntity<ApiResponse<Iterable<ArrematanteResponseDto>>> getAllByLeilaoId(@PathVariable Long leilaoId) {
        var arrematantes = arrematanteService.getAllByLeilaoId(leilaoId)
                .stream()
                .map(ArrematanteResponseDto::new)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(arrematantes, "Arrematantes do leilão obtidos com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ArrematanteResponseDto>> delete(@PathVariable Long id) {
        var arrematanteDeletado = arrematanteService.delete(id);
        var responseDto = new ArrematanteResponseDto(arrematanteDeletado);

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Arrematante deletado com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ArrematanteResponseDto>> update(@PathVariable Long id, @RequestBody @Valid ArrematanteRequestDto data) {
        var arrematanteAtualizado = arrematanteService.update(id, data);
        var responseDto = new ArrematanteResponseDto(arrematanteAtualizado);

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Arrematante atualizado com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ArrematanteDetalhadoResponseDto>> getById(@PathVariable Long id) {
        var arrematante = arrematanteService.getById(id);
        var responseDto = new ArrematanteDetalhadoResponseDto(arrematante);

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Arrematante obtido com sucesso"));

    }
}
