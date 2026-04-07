package br.com.boleiroOn.config.infra.email.controller;

import br.com.boleiroOn.config.infra.email.entity.EmailEntity;
import br.com.boleiroOn.config.infra.email.service.EmailService;
import br.com.boleiroOn.config.infra.email.service.EnviarEmailVerificationService;
import br.com.boleiroOn.shared.dto.ApiResponse;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/email")
@AllArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final EnviarEmailVerificationService enviarEmailVerificationService;

    
    @PostMapping("/send")
    public  void enviarEmail(@RequestBody EmailEntity email) throws MessagingException {
        emailService.enviarEmail(email);
    }

    @GetMapping("/validar-email")
    public ResponseEntity<ApiResponse<String>> validarEmail(@RequestParam String token) {
        enviarEmailVerificationService.validarEmail(token);
        return ResponseEntity.ok(ApiResponse.success("E-mail validado com sucesso!", "E-mail validado com sucesso"));
    }
}
