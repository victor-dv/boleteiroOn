package br.com.boleiroOn.config.infra.email.service;

import br.com.boleiroOn.config.infra.email.entity.EmailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnviarRecuperacaoSenhaService {

    private final EmailService emailService;

    @Value("${app.email.intern}")
    private String emailRemetente;

    @Value("${app.front.url}")
    private String frontUrl;

    public void enviarEmailRecuperacao(String nomeUsuario, String emailDestino, String token) {

        String linkRecuperacao = frontUrl + "redefinir-senha?token=" + token;

        String html = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8">
              <title>Recuperação de Senha - BoleiroOn</title>
              <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f5; color: #09090b; margin: 0; padding: 20px; }
                .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.05); }
                .header { background-color: #18181b; padding: 20px; text-align: center; color: #ffffff; }
                .content { padding: 30px; line-height: 1.6; }
                .highlight-box { background-color: #fefce8; border-left: 4px solid #eab308; padding: 15px; margin: 20px 0; font-size: 14px; }
                .button { background-color: #dc2626; color: #ffffff !important; text-decoration: none; padding: 14px 28px; border-radius: 6px; font-weight: bold; display: inline-block; margin: 20px 0; transition: background-color 0.2s; }
                .button:hover { background-color: #b91c1c; }
                .footer { background-color: #f4f4f5; padding: 20px; text-align: center; font-size: 12px; color: #71717a; border-top: 1px solid #e4e4e7; }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header"><h2>Recuperação de Senha</h2></div>
                <div class="content">
                  <p>Olá, <strong>%s</strong>,</p>
                  <p>Recebemos uma solicitação para redefinir a senha da sua conta no <strong>BoleiroOn</strong>.</p>
                  <p>Clique no botão abaixo para criar uma nova senha:</p>
                  
                  <div style="text-align: center;">
                    <a href="%s" class="button" target="_blank">Redefinir Minha Senha</a>
                  </div>

                  <div class="highlight-box">
                    <strong>Aviso de Segurança:</strong> Por motivos de segurança, este link é de uso único e irá expirar automaticamente em <strong>2 horas</strong>.
                  </div>
                  
                  <p style="font-size: 13px; color: #52525b; margin-top: 30px;">
                    Se o botão não funcionar, copie e cole a URL abaixo no seu navegador:<br>
                    <a href="%s" style="color: #dc2626; word-break: break-all;">%s</a>
                  </p>
                  
                  <p style="font-size: 13px; color: #52525b; margin-top: 20px;">
                    Se você não solicitou esta recuperação, por favor ignore este e-mail. Sua senha permanecerá inalterada.
                  </p>
                </div>
                <div class="footer"><p>Este é um e-mail automático do sistema BoleiroOn. Por favor, não responda.</p></div>
              </div>
            </body>
            </html>
            """.formatted(
                nomeUsuario,
                linkRecuperacao,
                linkRecuperacao,
                linkRecuperacao
        );

        EmailEntity novaMensagem = new EmailEntity();
        novaMensagem.setFrom("NexLeilões <" + emailRemetente + ">");
        novaMensagem.setTo(emailDestino);
        novaMensagem.setSubject("Recuperação de Senha - NexLeilões");
        novaMensagem.setText(html);

        emailService.enviarEmail(novaMensagem);
    }
}