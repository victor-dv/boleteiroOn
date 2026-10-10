package br.com.boleiroOn.domain.lote.entity;

import br.com.boleiroOn.domain.leilao.entity.LeilaoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "lotes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "leilao_id", nullable = false)
    private LeilaoEntity leilao;

    private Integer numeroLote;

    private String descricao;

    @Column(name = "lance_inicial")
    private BigDecimal valorInicial;
    @Column(name = "valor_avaliacao")
    private BigDecimal valorAvaliacao;

}
