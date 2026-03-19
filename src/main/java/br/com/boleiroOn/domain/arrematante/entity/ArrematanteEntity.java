    package br.com.boleiroOn.domain.arrematante.entity;

    import br.com.boleiroOn.domain.leilao.entity.LeilaoEntity;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Entity
    @Table(name = "arrematantes")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ArrematanteEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "leilao_id", nullable = false)
        private LeilaoEntity leilao;

        @Column(nullable = false)
        private String nome;

        @Column(nullable = false)
        private Integer placa;

        private String telefone;

        private String celular;

        private String endereco;

        @Column(nullable = false)
        private String email;

        @Column(name = "url_foto_documento", length = 500)
        private String urlFotoDocumento;

        private String documento;
        private String rg;
        private String cep;
        private String cidade;
        private String uf;
    }