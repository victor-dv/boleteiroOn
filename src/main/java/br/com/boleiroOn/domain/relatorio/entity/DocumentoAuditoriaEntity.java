package br.com.boleiroOn.domain.relatorio.entity;

import br.com.boleiroOn.domain.arrematante.entity.ArrematanteEntity;
import br.com.boleiroOn.domain.lote.entity.LoteEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_auditoria", indexes = {
        @Index(name = "idx_auditoria_leilao", columnList = "leilao_id"),
        @Index(name = "idx_auditoria_data", columnList = "data_envio")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentoAuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "leilao_id", nullable = false)
    private Long leilaoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrematante_id", nullable = false)
    private ArrematanteEntity arrematante;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private LoteEntity lote;
    @Column(name = "tipo_documento", nullable = false, length = 50)
    private String tipoDocumento;
    @Column(name = "url_s3", nullable = false, length = 500)
    private String urlS3;
    @Column(name = "status_email", nullable = false, length = 20)
    private String statusEmail;
    @CreationTimestamp
    @Column(name = "data_envio", nullable = false, updatable = false)
    private LocalDateTime dataEnvio;
}
