package br.com.boleiroOn.config.infra.relatorio.repository;

import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioAuditoriaDto;
import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioExcelDto;
import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioFinanceiroDto;
import br.com.boleiroOn.config.infra.relatorio.dto.RelatorioResumoDto;
import br.com.boleiroOn.config.infra.relatorio.entity.DocumentoAuditoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatorioRepository extends JpaRepository<DocumentoAuditoriaEntity, Long> {

    @Query("""
           SELECT new br.com.boleiroOn.config.infra.relatorio.dto.RelatorioResumoDto(
               l.id,
               l.nome,
               (SELECT COUNT(lt) FROM LoteEntity lt WHERE lt.leilao.id = l.id),
               (SELECT COUNT(a) FROM ArrematacaoEntity a WHERE a.lote.leilao.id = l.id),
               (SELECT SUM(a.valorArrematacao) FROM ArrematacaoEntity a WHERE a.lote.leilao.id = l.id),
               (SELECT SUM(a.valorComissao) FROM ArrematacaoEntity a WHERE a.lote.leilao.id = l.id)
           )
           FROM LeilaoEntity l
           WHERE l.id = :leilaoId
           """)
    RelatorioResumoDto buscarResumoLeilao(@Param("leilaoId") Long leilaoId);

    @Query("""
           SELECT new br.com.boleiroOn.config.infra.relatorio.dto.RelatorioFinanceiroDto(
               a.id,
               COALESCE(ar.nome, 'Venda Online'),
               lt.descricao,
               lt.numeroLote,
               a.valorArrematacao,
               a.valorComissao,
               a.status,
               a.dataArrematacao
           )
           FROM ArrematacaoEntity a
           LEFT JOIN a.arrematante ar
           JOIN a.lote lt
           WHERE lt.leilao.id = :leilaoId
           ORDER BY a.dataArrematacao DESC
           """)
    List<RelatorioFinanceiroDto> buscarRelatorioFinanceiro(@Param("leilaoId") Long leilaoId);

    @Query("""
           SELECT new br.com.boleiroOn.config.infra.relatorio.dto.RelatorioAuditoriaDto(
               d.id,
               COALESCE(ar.nome, 'Venda Online'),
               lt.descricao,
               lt.numeroLote,
               d.tipoDocumento,
               d.statusEmail,
               d.dataEnvio,
               d.urlS3
           )
           FROM DocumentoAuditoriaEntity d
           LEFT JOIN d.arrematante ar
           JOIN d.lote lt
           WHERE d.leilaoId = :leilaoId
           ORDER BY d.dataEnvio DESC
           """)
    List<RelatorioAuditoriaDto> buscarRelatorioAuditoria(@Param("leilaoId") Long leilaoId);

    @Query("""
           SELECT new br.com.boleiroOn.config.infra.relatorio.dto.RelatorioExcelDto(
               lt.numeroLote,
               ar.nome,
               ar.email,
               ar.documento,
               ar.celular,
               ar.cep,
               ar.endereco,
               ar.cidade,
               ar.uf,
               a.valorArrematacao,
               a.valorComissao,
               (a.valorArrematacao + a.valorComissao)
           )
           FROM ArrematacaoEntity a
           JOIN a.arrematante ar
           JOIN a.lote lt
           WHERE lt.leilao.id = :leilaoId
           ORDER BY lt.numeroLote ASC
           """)
    List<RelatorioExcelDto> buscarDadosParaExcel(@Param("leilaoId") Long leilaoId);
}
