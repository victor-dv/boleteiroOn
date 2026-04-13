package br.com.boleiroOn.config.infra.email.service;

import br.com.boleiroOn.config.infra.email.entity.EmailEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnviarDefinicaoSenhaService {

    private final EmailService emailService;

    @Value("${app.email.intern}")
    private String emailRemetente;

    @Value("${app.front.url}")
    private String frontUrl;

    public void enviarEmailConvite(String nomeUsuario, String emailDestino, String token) {

        String linkDefinicao = frontUrl + "/definir-senha?token=" + token;

        String html = """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
              <meta charset="UTF-8">
              <title>Convite de Acesso - BoleiroOn</title>
              <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f5; color: #09090b; margin: 0; padding: 20px; }
                .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.05); }
                .header { background-color: #18181b; padding: 20px; text-align: center; color: #ffffff; }
                .content { padding: 30px; line-height: 1.6; }
                .highlight-box { background-color: #fefce8; border-left: 4px solid #eab308; padding: 15px; margin: 20px 0; font-size: 14px; }
                .button { background-color: #2563eb; color: #ffffff !important; text-decoration: none; padding: 14px 28px; border-radius: 6px; font-weight: bold; display: inline-block; margin: 20px 0; transition: background-color 0.2s; }
                .button:hover { background-color: #1d4ed8; }
                .footer { background-color: #f4f4f5; padding: 20px; text-align: center; font-size: 12px; color: #71717a; border-top: 1px solid #e4e4e7; }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header"><h2>Bem-vindo ao BoleiroOn</h2></div>
                <div class="content">
                  <p>Olá, <strong>%s</strong>,</p>
                  <p>Um perfil de administrador/colaborador foi criado para você no sistema de gestão de leilões <strong>BoleiroOn</strong>.</p>
                  <p>Para ativar sua conta e liberar seu acesso, você precisa definir sua senha inicial clicando no botão abaixo:</p>
                  
                  <div style="text-align: center;">
                    <a href="%s" class="button" target="_blank">Definir Minha Senha</a>
                  </div>

                  <div class="highlight-box">
                    <strong>Aviso de Segurança:</strong> Por motivos de segurança, este link de ativação é de uso único e irá expirar automaticamente em <strong>24 horas</strong>.
                  </div>
                  
                  <p style="font-size: 13px; color: #52525b; margin-top: 30px;">
                    Se o botão não funcionar, copie e cole a URL abaixo no seu navegador:<br>
                    <a href="%s" style="color: #2563eb; word-break: break-all;">%s</a>
                  </p>
                </div>
                <div class="footer"><p>Este é um e-mail automático do sistema BoleiroOn. Por favor, não responda.</p></div>
              </div>
            </body>
            </html>
            """.formatted(
                nomeUsuario,
                linkDefinicao,
                linkDefinicao,
                linkDefinicao
        );

        EmailEntity novaMensagem = new EmailEntity();
        novaMensagem.setFrom("BoleiroOn <" + emailRemetente + ">");
        novaMensagem.setTo(emailDestino);
        novaMensagem.setSubject("Convite de Acesso - Defina sua senha no BoleiroOn");
        novaMensagem.setText(html);

        emailService.enviarEmail(novaMensagem);
    }
}