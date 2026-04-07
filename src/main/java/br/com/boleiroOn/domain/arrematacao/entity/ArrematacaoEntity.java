package br.com.boleiroOn.domain.arrematacao.entity;

import br.com.boleiroOn.domain.arrematacao.enums.StatusPagamentoArrematacao;
import br.com.boleiroOn.domain.arrematante.entity.ArrematanteEntity;
import br.com.boleiroOn.domain.lote.entity.LoteEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "arrematacoes")
public class ArrematacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "lote_id", nullable = false)
    private LoteEntity lote;
    @ManyToOne
    @JoinColumn(name = "arrematante_id")
    private ArrematanteEntity arrematante;

    @Column(name = "venda_online", nullable = false)
    private boolean vendaOnline = false;

    @Column(name = "venda_condicional", nullable = false)
    private boolean vendaCondicional = false;

    @Column(name = "valor_arrematacao", nullable = false)
    private BigDecimal valorArrematacao;

    @Column(name = "valor_comissao", nullable = false)
    private BigDecimal valorComissao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamentoArrematacao status = StatusPagamentoArrematacao.PENDENTE_PAGAMENTO;

    @Column(name = "data_arrematacao", nullable = false)
    private LocalDateTime dataArrematacao = LocalDateTime.now();

    @Column(name = "url_foto_assinatura")
    private String urlFotoAssinatura;

    @Column (name = "assinatura_base64", length = 100000)
    private String assinaturaBase64;

    @Column(name = "url_auto_pdf")
    private String urlAutoPdf;

    @PrePersist
    @PreUpdate
    public void calcularComissao() {
        if (this.valorArrematacao != null) {
            this.valorComissao = this.valorArrematacao.multiply(new BigDecimal("0.05"));
        }
    }
}
