package br.com.boleiroOn.config.infra.email.entity;

import br.com.boleiroOn.domain.arrematante.entity.ArrematanteEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_validacao_arrematante")
@Data
@NoArgsConstructor
public class VerificationEmailToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;
    @OneToOne
    @JoinColumn(name = "arrematante_id", nullable = false)
    private ArrematanteEntity arrematante;
    private LocalDateTime dataExpiracao;

    public VerificationEmailToken(String token, ArrematanteEntity arrematante, LocalDateTime dataExpiracao) {
        this.token = token;
        this.arrematante = arrematante;
        this.dataExpiracao = LocalDateTime.now().plusHours(1);
    }
    public boolean estaExpirado(){
        return LocalDateTime.now().isAfter(this.dataExpiracao);
    }

}
