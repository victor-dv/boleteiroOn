package br.com.boleiroOn.domain.arrematacao.repository;

import br.com.boleiroOn.domain.arrematacao.dto.ArrematacaoFeedDto;
import br.com.boleiroOn.domain.arrematacao.entity.ArrematacaoEntity;
import br.com.boleiroOn.domain.arrematacao.enums.StatusPagamentoArrematacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArrematacaoRepository extends JpaRepository<ArrematacaoEntity, Long> {
    boolean existsByLoteIdAndStatusNot(Long loteId, StatusPagamentoArrematacao status);


    @Query("""
    SELECT new br.com.boleiroOn.domain.arrematacao.dto.ArrematacaoFeedDto(
        a.id, 
        l.numeroLote, 
        l.descricao, 
        a.vendaOnline,
        COALESCE(arr.nome, 'Venda Online'), 
        arr.placa,
        COALESCE(arr.urlFotoDocumento, 'https://seu-s3.com/avatar-padrao.png'), 
        a.valorArrematacao, 
        a.status,
        a.urlFotoAssinatura
    ) 
    FROM ArrematacaoEntity a 
    JOIN a.lote l 
    LEFT JOIN a.arrematante arr 
    WHERE l.leilao.id = :leilaoId 
    ORDER BY a.id DESC
""")
    List<ArrematacaoFeedDto> buscarUltimasArrematacoesDoLeilao(Long leilaoId);

    boolean existsByArrematanteId(Long arrematanteId);}
