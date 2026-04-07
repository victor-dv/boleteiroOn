package br.com.boleiroOn.config.infra.email.service;

import br.com.boleiroOn.config.infra.email.entity.EmailEntity;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService(@Value("${resend.api.key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public void enviarEmail(EmailEntity email) {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(email.getFrom())
                .to(email.getTo())
                .subject(email.getSubject())
                .html(email.getText())
                .build();
        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println("E-mail enviado com sucesso! ID: " + data.getId());
        } catch (ResendException e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao disparar e-mail via Resend", e);
        }
    }
}