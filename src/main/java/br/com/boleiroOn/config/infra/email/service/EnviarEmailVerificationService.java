package br.com.boleiroOn.config.infra.email.service;

import br.com.boleiroOn.config.infra.email.entity.EmailEntity;
import br.com.boleiroOn.config.infra.email.entity.VerificationToken;
import br.com.boleiroOn.config.infra.email.repository.VerificationTokenRepository;
import br.com.boleiroOn.domain.arrematante.entity.ArrematanteEntity;
import br.com.boleiroOn.domain.arrematante.repository.ArrematanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnviarEmailVerificationService {

    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ArrematanteRepository arrematanteRepository;

    @Value("${app.email.verification}")
    private String emailVerification;

    public void enviarEmailDeValidacao(ArrematanteEntity arrematante, String token){
        String link = "http://localhost:8080/api/email/validar-email?token=" + token;

        EmailEntity email = new EmailEntity();
        email.setFrom(emailVerification);
        email.setTo(arrematante.getEmail());
        email.setSubject("Validação de Cadastro - Leilão " + arrematante.getLeilao().getNome());
        email.setText("<h1>Olá " + arrematante.getNome() + "!</h1>" +
                "<p>Para confirmar sua participação no leilão, valide seu e-mail clicando abaixo:</p>" +
                "<a href='" + link + "'>Validar meu e-mail</a>");

        emailService.enviarEmail(email);
    }

    public void validarEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token de validação inválido."));

        if (verificationToken.estaExpirado()){
            verificationTokenRepository.delete(verificationToken);
            throw new RuntimeException("Token de validação expirado.");
        }
        ArrematanteEntity arrematante = verificationToken.getArrematante();
        arrematante.setEmailValidado(true);
        arrematanteRepository.save(arrematante);


        verificationTokenRepository.delete(verificationToken);

    }
}
