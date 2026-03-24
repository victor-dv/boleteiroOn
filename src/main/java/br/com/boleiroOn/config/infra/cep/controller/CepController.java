package br.com.boleiroOn.config.infra.cep.controller;

import br.com.boleiroOn.config.infra.cep.client.ViaCepClient;
import br.com.boleiroOn.config.infra.cep.dto.CepResponseDto;
import br.com.boleiroOn.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/consulta")
@RequiredArgsConstructor
public class CepController {
    private final ViaCepClient viaCepClient;

    @GetMapping("/cep/{cep}")
    public ResponseEntity<ApiResponse<CepResponseDto>> buscarCep(@PathVariable String cep) {
        String cepLimpo = cep.replaceAll("\\D", "");
        CepResponseDto dadosCep = viaCepClient.buscarCep(cepLimpo);

        return ResponseEntity.ok(ApiResponse.success(dadosCep, "CEP consultado com sucesso"));
    }

}
