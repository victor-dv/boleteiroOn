package br.com.boleiroOn.domain.lote.entity;

import br.com.boleiroOn.domain.leilao.entity.LeilaoEntity;
import jakarta.persistence.*;
import lombok.*;

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

}
