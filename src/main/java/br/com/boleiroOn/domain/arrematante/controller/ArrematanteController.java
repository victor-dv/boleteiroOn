package br.com.boleiroOn.domain.arrematante.controller;

import br.com.boleiroOn.domain.arrematante.dto.ArrematanteRequestDto;
import br.com.boleiroOn.domain.arrematante.dto.ArrematanteResponseDto;
import br.com.boleiroOn.domain.arrematante.service.ArrematanteService;
import br.com.boleiroOn.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
