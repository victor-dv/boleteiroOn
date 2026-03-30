package br.com.boleiroOn.domain.arrematacao.repository;

import br.com.boleiroOn.domain.arrematacao.dto.ArrematacaoFeedDto;
import br.com.boleiroOn.domain.arrematacao.entity.ArrematacaoEntity;
import br.com.boleiroOn.domain.arrematacao.enums.StatusArrematacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArrematacaoRepository extends JpaRepository<ArrematacaoEntity, Long> {
    boolean existsByLoteIdAndStatusNot(Long loteId, StatusArrematacao status);


    @Query("""
    SELECT new br.com.boleiroOn.domain.arrematacao.dto.ArrematacaoFeedDto(
        a.id, 
        l.numeroLote, 
        l.descricao, 
        COALESCE(arr.nome, 'Venda Online'), 
        COALESCE(arr.urlFotoDocumento, 'https://seu-s3.com/avatar-padrao.png'), 
        a.valorArrematacao, 
        a.status
    ) 
    FROM ArrematacaoEntity a 
    JOIN a.lote l 
    LEFT JOIN a.arrematante arr 
    WHERE l.leilao.id = :leilaoId 
    ORDER BY a.id DESC
""")
    List<ArrematacaoFeedDto> buscarUltimasArrematacoesDoLeilao(Long leilaoId);
}
