package br.com.boleiroOn.config.infra.email.service;

import br.com.boleiroOn.config.infra.email.entity.EmailEntity;
import br.com.boleiroOn.config.infra.email.entity.VerificationEmailToken;
import br.com.boleiroOn.config.infra.email.repository.VerificationEmailTokenRepository;
import br.com.boleiroOn.domain.arrematante.entity.ArrematanteEntity;
import br.com.boleiroOn.domain.arrematante.repository.ArrematanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnviarEmailVerificationService {

    private final EmailService emailService;
    private final VerificationEmailTokenRepository verificationTokenRepository;
    private final ArrematanteRepository arrematanteRepository;

    @Value("${app.email.verification}")
    private String emailVerification;

    public void enviarEmailDeValidacao(ArrematanteEntity arrematante, String token){
        String link = "https://gleaming-comfort-production-f600.up.railway.app/api/email/validar-email?token=" + token;

        String htmlTemplate = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8">
              <title>Validação de Cadastro</title>
            </head>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f5; color: #09090b; margin: 0; padding: 20px; -webkit-font-smoothing: antialiased;">
              <table width="100%%" cellpadding="0" cellspacing="0" role="presentation">
                <tr>
                  <td align="center">
                    <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; border: 1px solid #e4e4e7;">
                      
                      <div style="background-color: #18181b; padding: 20px; text-align: center; color: #ffffff;">
                        <h2 style="margin: 0; font-size: 24px;">Validação de E-mail</h2>
                      </div>
                      
                      <div style="padding: 30px; line-height: 1.6; font-size: 16px;">
                        <p style="margin-top: 0;">Olá, <strong>%s</strong>,</p>
                        <p>Falta pouco! Para concluir o seu cadastro e confirmar a sua participação no leilão <strong>%s</strong>, por favor, valide o seu endereço de e-mail clicando no botão abaixo:</p>
                        
                        <div style="text-align: center; margin: 35px 0;">
                          <a href="%s" target="_blank" style="background-color: #2563eb; color: #ffffff; text-decoration: none; padding: 14px 28px; border-radius: 6px; font-weight: bold; display: inline-block;">
                            Validar meu e-mail
                          </a>
                        </div>
            
                        <p style="font-size: 14px; color: #52525b; margin-bottom: 0;">
                          Se você não se cadastrou em nossa plataforma, pode ignorar este e-mail com segurança.
                        </p>
                      </div>
                    </div>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(
                arrematante.getNome(),
                arrematante.getLeilao().getNome(),
                link
        );

        EmailEntity email = new EmailEntity();
        email.setFrom("BoleiroOn <" + emailVerification + ">");
        email.setTo(arrematante.getEmail());
        email.setSubject("Validação de Cadastro - Leilão " + arrematante.getLeilao().getNome());

        email.setText(htmlTemplate);

        emailService.enviarEmail(email);
    }

    public void validarEmail(String token) {
        VerificationEmailToken verificationToken = verificationTokenRepository.findByToken(token)
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