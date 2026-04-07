package br.com.boleiroOn.domain.arrematante.service;

import br.com.boleiroOn.config.infra.email.repository.VerificationTokenRepository;
import br.com.boleiroOn.config.infra.email.service.EmailService;
import br.com.boleiroOn.config.infra.email.entity.VerificationToken;
import br.com.boleiroOn.config.infra.email.service.EnviarEmailVerificationService;
import br.com.boleiroOn.domain.arrematacao.repository.ArrematacaoRepository;
import br.com.boleiroOn.domain.arrematante.dto.ArrematanteRequestDto;
import br.com.boleiroOn.domain.arrematante.entity.ArrematanteEntity;
import br.com.boleiroOn.domain.arrematante.repository.ArrematanteRepository;
import br.com.boleiroOn.domain.leilao.repository.LeilaoRepository;
import br.com.boleiroOn.shared.exception.BusinessException;
import br.com.boleiroOn.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArrematanteService {

    private final ArrematanteRepository arrematanteRepository;
    private final LeilaoRepository leilaoRepository;
    private final ArrematacaoRepository arrematacaoRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EnviarEmailVerificationService enviarEmailVerificationService;

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
        arrematante.setEmailValidado(false);

        arrematante.setDocumento(data.documento());
        arrematante.setRg(data.rg());
        arrematante.setCep(data.cep());
        arrematante.setCidade(data.cidade());
        arrematante.setUf(data.uf());

        arrematante = arrematanteRepository.save(arrematante);

        String tokenUUID = UUID.randomUUID().toString();
        VerificationToken verificationToken1 = new VerificationToken(tokenUUID, arrematante, null);
        verificationTokenRepository.save(verificationToken1);
        enviarEmailVerificationService.enviarEmailDeValidacao(arrematante, tokenUUID);
        return arrematante;

    }

    public List<ArrematanteEntity> getAllByLeilaoId(Long leilaoId) {
        var leilao = leilaoRepository.findById(leilaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado."));
        return arrematanteRepository.findAllByLeilaoId(leilaoId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ArrematanteEntity delete (Long id) {
        var arr = arrematanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arrematante não encontrado."));
        var hasArrematacao = arrematacaoRepository.existsByArrematanteId(id);
        if (hasArrematacao) {
            throw new BusinessException("Não é possível excluir um arrematante que possui arrematações associadas.");
        }

        arrematanteRepository.delete(arr);
        return arr;
    }

    public ArrematanteEntity update(Long arrematanteId, ArrematanteRequestDto data) {
        var arr = arrematanteRepository.findById(arrematanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Arrematante não encontrado."));

        arr.setNome(data.nome() != null ? data.nome() : arr.getNome());
        arr.setPlaca(data.placa() != null ? data.placa() : arr.getPlaca());
        arr.setTelefone(data.telefone());
        arr.setCelular(data.celular() != null ? data.celular() : arr.getCelular());
        arr.setEmail(data.email() != null ? data.email() : arr.getEmail());
        arr.setUrlFotoDocumento(data.urlFotoDocumento());
        arr.setDocumento(data.documento() != null ? data.documento() : arr.getDocumento());
        arr.setRg(data.rg());
        arr.setCep(data.cep() != null ? data.cep() : arr.getCep());
        arr.setCidade(data.cidade() != null ? data.cidade() : arr.getCidade());
        arr.setEndereco(data.endereco() != null ? data.endereco() : arr.getEndereco());
        arr.setUf(data.uf() != null ? data.uf() : arr.getUf());

        return arrematanteRepository.save(arr);
    }

    public ArrematanteEntity getById(Long id) {
        return arrematanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arrematante não encontrado."));
    }

}
