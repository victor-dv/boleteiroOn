package br.com.boleiroOn.domain.arrematante.service;

import br.com.boleiroOn.domain.arrematante.dto.ArrematanteRequestDto;
import br.com.boleiroOn.domain.arrematante.entity.ArrematanteEntity;
import br.com.boleiroOn.domain.arrematante.repository.ArrematanteRepository;
import br.com.boleiroOn.domain.leilao.repository.LeilaoRepository;
import br.com.boleiroOn.shared.exception.BusinessException;
import br.com.boleiroOn.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArrematanteService {

    private final ArrematanteRepository arrematanteRepository;
    private final LeilaoRepository leilaoRepository;

    @Transactional
    public ArrematanteEntity create(ArrematanteRequestDto data) {
        var leilao = leilaoRepository.findById(data.leilaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado."));

        if (arrematanteRepository.existsByLeilaoIdAndPlaca(data.leilaoId(), data.placa())) {
            throw new BusinessException("Esta placa já está em uso neste leilão.");
        }

        ArrematanteEntity arrematante = new ArrematanteEntity();
        arrematante.setLeilao(leilao);
        arrematante.setNome(data.nome());
        arrematante.setPlaca(data.placa());
        arrematante.setEmail(data.email());
        arrematante.setTelefone(data.telefone());
        arrematante.setCelular(data.celular());
        arrematante.setEndereco(data.endereco());
        arrematante.setUrlFotoDocumento(data.urlFotoDocumento());

        arrematante.setDocumento(data.documento());
        arrematante.setRg(data.rg());
        arrematante.setCep(data.cep());
        arrematante.setCidade(data.cidade());
        arrematante.setUf(data.uf());

        return arrematanteRepository.save(arrematante);
    }
}
