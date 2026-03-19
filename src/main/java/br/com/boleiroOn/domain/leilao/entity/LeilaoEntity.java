package br.com.boleiroOn.domain.leilao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "leiloes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeilaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private boolean status = true;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    private String comitenteNome;
    private String comitenteDocumento;
    private String edital;
    private String processoAdministrativo;
    private String leiloeiroNome;
    private String leiloeiroMatricula;
    private String leiloeiroDocumento;
    private String cidadeUfPagamento;
}
