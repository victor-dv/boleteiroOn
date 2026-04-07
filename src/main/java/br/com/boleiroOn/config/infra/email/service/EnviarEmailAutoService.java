package br.com.boleiroOn.config.infra.email.service;

import br.com.boleiroOn.config.infra.email.entity.EmailEntity;
import br.com.boleiroOn.domain.arrematacao.entity.ArrematacaoEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EnviarEmailAutoService {

    private final EmailService emailService;

    @Value("${app.email.boleto}")
    private String emailRemetente;

    public void enviarEmailComLinkPdf(ArrematacaoEntity arrematacao, String linkPdf) {
        String nomeArrematante = arrematacao.getArrematante().getNome();
        String emailArrematante = arrematacao.getArrematante().getEmail();
        var arrematante = arrematacao.getArrematante();
        String nomeLeilao = arrematante.getLeilao().getNome();

        String numeroLote = String.format("%03d", arrematacao.getLote().getNumeroLote());

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String valorFormatado = formatoMoeda.format(arrematacao.getValorArrematacao());

        String html = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8">
              <title>Seu Auto de Arrematação</title>
              <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f5; color: #09090b; margin: 0; padding: 20px; }
                .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; }
                .header { background-color: #18181b; padding: 20px; text-align: center; color: #ffffff; }
                .content { padding: 30px; line-height: 1.6; }
                .highlight-box { background-color: #f4f4f5; border-left: 4px solid #2563eb; padding: 15px; margin: 20px 0; }
                .button { background-color: #2563eb; color: #ffffff !important; text-decoration: none; padding: 14px 28px; border-radius: 6px; font-weight: bold; display: inline-block; margin: 20px 0; }
                .footer { background-color: #f4f4f5; padding: 20px; text-align: center; font-size: 12px; color: #71717a; }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header"><h2>Auto de Arrematação Disponível</h2></div>
                <div class="content">
                  <p>Olá, <strong>%s</strong>,</p>
                  <p>Parabéns pela sua arrematação! O seu auto de arrematação presencial foi devidamente assinado, registrado e o documento já está disponível para acesso.</p>
            
                  <div class="highlight-box">
                    <p><strong>Leilão:</strong> %s</p>
                    <p><strong>Lote Arrematado:</strong> %s</p>
                    <p><strong>Valor:</strong> %s</p>
                  </div>
                  
                  <div style="text-align: center;">
                    <a href="%s" class="button" target="_blank">Acessar Documento em PDF</a>
                  </div>
                </div>
                <div class="footer"><p>Este é um e-mail automático. Por favor, não responda.</p></div>
              </div>
            </body>
            </html>
            """.formatted(
                nomeArrematante,
                nomeLeilao,
                numeroLote,
                valorFormatado,
                linkPdf
        );

        EmailEntity novaMensagem = new EmailEntity();
        novaMensagem.setFrom("BoleiroOn <" + emailRemetente + ">");
        novaMensagem.setTo(emailArrematante);
        novaMensagem.setSubject("Seu Auto de Arrematação foi gerado - Lote " + numeroLote);
        novaMensagem.setText(html);

        emailService.enviarEmail(novaMensagem);
    }
}