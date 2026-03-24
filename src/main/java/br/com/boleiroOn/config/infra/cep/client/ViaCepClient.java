package br.com.boleiroOn.config.infra.cep.client;

import br.com.boleiroOn.config.infra.cep.dto.CepResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("/{cep}/json/")
    CepResponseDto buscarCep(@PathVariable("cep") String cep);
}
